package com.github.resource4j.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.parsers.ResourceParsers;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.refreshable.cache.*;
import com.github.resource4j.refreshable.cache.impl.BasicValueCache;
import com.github.resource4j.resources.AbstractResources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.values.GenericOptionalString;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static com.github.resource4j.refreshable.cache.CacheRecord.initial;
import static com.github.resource4j.resources.context.ResourceResolutionContext.*;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.StreamSupport.stream;

/**
 * 1. Push request ("bundle.k1", ctx)
 * 2. Map ("bundle.k1", ctx) -> ("bundle-ctx-A", "bundle-ctx-B")
 * 3. Load "bundle-ctx-A" -> ("bundle-ctx-A:bundle.k1", "bundle-ctx-A:bundle.k2"...)
 * 4. Reduce ("bundle-ctx-A:bundle.k1","bundle-ctx-B:bundle.k1" -> "bundle-ctx-A:bundle.k1")
 * 5.
 */
public class RefreshableResources extends AbstractResources {

    private Cache<ResolvedKey, CachedValue> values = new BasicValueCache<>();

    private Cache<ResolvedName, CachedBundle> bundles = new BasicValueCache<>();

    private Cache<ResolvedName, CachedObject> objects = new BasicValueCache<>();

    private List<ResourceObjectProvider> providers;

    private Clock clock = Clock.systemUTC();

    private ReentrantLock requestLock = new ReentrantLock();

    private Map<ResolvedKey, Future<CachedValue>> valueRequests = new HashMap<>();

    private Map<ResolvedName, Future<CachedBundle>> bundleRequests = new HashMap<>();

	private Map<ResolvedName, Future<CachedObject>> objectRequests = new HashMap<>();

	private ExecutorService valueQueue = Executors.newFixedThreadPool(2, new NamedThreadFactory("value-loader"));

    private ExecutorService bundleQueue = Executors.newFixedThreadPool(2, new NamedThreadFactory("bundle-loader"));

	private ExecutorService objectQueue = Executors.newFixedThreadPool(2, new NamedThreadFactory("object-loader"));

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger();
        private final String name;
        private static final String THREAD_NAME_PATTERN = "%s-%d";
        public NamedThreadFactory(String name) {
            this.name = name;
        }
        @Override
        public Thread newThread(Runnable r) {
            final String threadName = String.format(THREAD_NAME_PATTERN, name, counter.incrementAndGet());
            return new Thread(r, threadName);

        }
    }

    private CachedValue loadValue(ResolvedKey resolvedKey) {
        String objectName = resolvedKey.key().getBundle().replace('.', '/');

        ResolvedName bundleName = new ResolvedName(objectName, resolvedKey.context());
        CacheRecord<CachedBundle> value = bundles.putIfAbsent(bundleName, initial());
        fetchIfNotInitialized(value, bundleName, bundleRequests, () -> {
            Future<CachedBundle> future = null;
            future = fireRequest(null, bundleQueue, () -> this.loadBundle(bundleName));
            return future;
        });

        if (value.is(CacheRecord.StateType.EXISTS)) {
            CachedBundle bundle = value.get();
            String valueString = bundle.get(resolvedKey.key().getId());
            return new CachedValue(valueString, bundle.source());

        }
        return new CachedValue(null, null);
    }

    private CachedBundle loadBundle(ResolvedName bundleName) {
        ResourceObject object = loadObjectNonRecursive(bundleName);
        try {
            Map<String, String> bundle = object.parsedTo(ResourceParsers.propertyMap()).asIs();
            return new CachedBundle(object.actualName(), bundle);
        } catch (Exception e) {
            return new CachedBundle(object.actualName(), null);
        }
    }

    private ResourceObject loadObjectNonRecursive(ResolvedName resolvedName) {
        CacheRecord<CachedObject> value = objects.putIfAbsent(resolvedName, initial());
        fetchIfNotInitialized(value, resolvedName, objectRequests, () -> fireAllRequests(resolvedName, null));
        return toResourceObject(resolvedName, value);
    }

    private <K, O> void cacheObject(K key, Future<O> future, CacheRecord<O> value) {
        try {
            O result = future.get();
            requestLock.lock();
            try {
                value.store(result, clock::millis);
                LOG.trace("Cached {} -> {}", key, result);
            }finally {
                requestLock.unlock();
            }
        } catch (InterruptedException | ExecutionException e) {
            requestLock.lock();
            try {
                value.fail(e);
            } finally {
                requestLock.unlock();
            }
        }
    }

    private OptionalString toOptionalString(ResolvedKey resolvedKey, CacheRecord<CachedValue> record) {
        OptionalString string;
        final ResourceKey key = resolvedKey.key();
        switch (record.state()) {
            case EXISTS:
                string = new GenericOptionalString(record.get().source(), key, record.get().value());
                break;
            case MISSING:
                string = new GenericOptionalString(null, key, null);
                break;
            case ERROR:
                string = new GenericOptionalString(null, key, null, record.error());
                break;
            default:
                throw new IllegalStateException(String.valueOf(record.state()));
        }
        LOG.debug("Returning {} -> {}", resolvedKey, string);
        return string;
    }

    @Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
	    ResolvedKey resolvedKey = new ResolvedKey(key, context);
        CacheRecord<CachedValue> value = values.putIfAbsent(resolvedKey, initial());
        // Cached value may be updated with loaded results so we use the double-check locking here
        fetchIfNotInitialized(value, resolvedKey, valueRequests, () -> {
            Future<CachedValue> future = null;
            // not submitted, so we'll create requests for this key and all parent contexts
            LinkedList<ResolvedKey> parentKeys = parentsOf(resolvedKey.context())
                    .map(ctx -> new ResolvedKey(resolvedKey.key(), ctx))
                    .collect(toCollection(LinkedList::new));
            for (Iterator<ResolvedKey> iterator = parentKeys.descendingIterator(); iterator.hasNext(); ) {
                ResolvedKey parentKey = iterator.next();
                future = fireRequest(future, valueQueue, () -> this.loadValue(parentKey));
                valueRequests.put(parentKey, future);
            }
            future = fireRequest(future, valueQueue, () -> this.loadValue(resolvedKey));
            valueRequests.put(resolvedKey, future);
            return future;
        });
        return toOptionalString(resolvedKey, value);
	}

    protected <K,O> void fetchIfNotInitialized(CacheRecord<O> value, K key,
                                               Map<K, Future<O>> existingRequests, Supplier<Future<O>> fetchTaskSupplier) {
        // Cached value may be updated with loaded results so we use the double-check locking here
        if (value.is(CacheRecord.StateType.PENDING)) {
            requestLock.lock();
            Future<O> future = null;
            try {
                if (value.is(CacheRecord.StateType.PENDING)) {
                    // check if request has already been submitted in another thread
                    future = existingRequests.get(key);
                    if (future == null) {
                        future = fetchTaskSupplier.get();
                    }
                }
            } finally {
                requestLock.unlock();
            }
            // future can be null if by second check above the value has already been loaded
            // if it's not null, then we are expecting new result and need to wait
            if (future != null) {
                cacheObject(key, future, value);
            }
        }
    }

	private ResourceObject toResourceObject(ResolvedName resolvedName, CacheRecord<CachedObject> record) {
		ResourceObject object;
		String name = resolvedName.name();
		switch (record.state()) {
			case EXISTS:
				object = record.get().get();
				break;
			case MISSING:
				throw new MissingResourceObjectException(name);
			case ERROR:
				throw new MissingResourceObjectException(record.error(), name);
			default:
				throw new IllegalStateException(String.valueOf(record.state()));
		}
        LOG.debug("Returning {} -> {}", resolvedName, object);
		return object;
	}

    private <O extends CachedResult> Future<O> fireRequest(Future<O> parent,
		ExecutorService queue,
        Callable<O> loader) {
        Future<O> future = queue.submit(loader);
	    return parent == null ? future : new FallbackFuture<>(future, parent, CachedResult::exists);
    }

	private CachedObject loadObject(ResourceObjectProvider provider, ResolvedName resolvedName) {
        try {
            long millis = clock.millis();
            ResourceObject object = provider.get(resolvedName.name(), resolvedName.context());
            LOG.trace("Request complete: {} loaded in {} ms from {}", resolvedName, clock.millis() - millis, provider);
            return new CachedObject(object);
        } catch (ResourceObjectAccessException e) {
            return new CachedObject(null);
        }
	}

	@Override
	public ResourceObject contentOf(String name, ResourceResolutionContext context) {
		ResolvedName resolvedKey = new ResolvedName(name, context);
		CacheRecord<CachedObject> value = objects.putIfAbsent(resolvedKey, initial());
        fetchIfNotInitialized(value, resolvedKey, objectRequests, () -> {
            Future<CachedObject> future = null;
            // not submitted, so we'll create requests for this key and all parent contexts
            LinkedList<ResolvedName> parentKeys = parentsOf(resolvedKey.context())
                    .map(ctx -> new ResolvedName(resolvedKey.name(), ctx))
                    .collect(toCollection(LinkedList::new));

            for (Iterator<ResolvedName> iterator = parentKeys.descendingIterator(); iterator.hasNext(); ) {
                ResolvedName parentKey = iterator.next();
                future = fireAllRequests(parentKey, future);
            }
            future = fireAllRequests(resolvedKey, future);
            return future;
        });
		return toResourceObject(resolvedKey, value);
	}

    protected Deque<ResourceObjectProvider> getSortedProviders() {
        return this.providers.stream()
                        .collect(toCollection(ArrayDeque::new));
    }

    protected Future<CachedObject> fireAllRequests(ResolvedName resolvedKey, Future<CachedObject> future) {
        Deque<ResourceObjectProvider> providers = getSortedProviders();
        for (Iterator<ResourceObjectProvider> pIterator = providers.descendingIterator(); pIterator.hasNext();) {
            ResourceObjectProvider provider = pIterator.next();
            future = fireRequest(future, objectQueue, () -> this.loadObject(provider, resolvedKey));
        }
        objectRequests.put(resolvedKey, future);
        return future;
    }

    public void setObjectProviders(List<ResourceObjectProvider> providers) {
        this.providers = providers;
    }

}

