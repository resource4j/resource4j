package com.github.resource4j.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.parsers.BundleParser;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.cache.CacheRecord;
import com.github.resource4j.refreshable.cache.CachedBundle;
import com.github.resource4j.refreshable.cache.CachedObject;
import com.github.resource4j.refreshable.cache.CachedResult;
import com.github.resource4j.refreshable.cache.CachedValue;
import com.github.resource4j.resources.AbstractResources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.values.GenericOptionalString;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static com.github.resource4j.refreshable.ResourcesConfiguratorBuilder.configure;
import static com.github.resource4j.refreshable.cache.CacheRecord.initial;
import static com.github.resource4j.resources.context.ResourceResolutionContext.parentsOf;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.util.stream.Collectors.toCollection;

public class RefreshableResources extends AbstractResources {

    private Cache<ResolvedKey, CachedValue> values;

    private Cache<ResolvedName, CachedBundle> bundles;

    private Cache<ResolvedName, CachedObject> objects;

    private List<ResourceObjectProvider> providers;

    private Clock clock = Clock.systemUTC();

    private ReentrantLock requestLock = new ReentrantLock();

    private Map<ResolvedKey, Future<CachedValue>> valueRequests = new HashMap<>();

    private Map<ResolvedName, Future<CachedBundle>> bundleRequests = new HashMap<>();

	private Map<ResolvedName, Future<CachedObject>> objectRequests = new HashMap<>();

	private ExecutorService valueQueue;

    private ExecutorService bundleQueue;

	private ExecutorService objectQueue;

    private List<BundleFormat> bundleFormats;

    private ResourceKey defaultBundle;

    private ResourceObjectRepositoryListener listener = event -> {
        LOG.info("Repository {} updated: clearing caches...", event.source());
        resetCaches();
    };

    public RefreshableResources() {
        this(configure().get());
    }

    public RefreshableResources(RefreshableResourcesConfigurator configurator) {
        configurator.configureSources(sources -> {
            this.providers = sources;
            for (ResourceObjectProvider provider : this.providers) {
                if (provider instanceof ResourceObjectRepository) {
                    ResourceObjectRepository repository = (ResourceObjectRepository) provider;
                    repository.addListener(listener);
                }
            }
        });
        configurator.configureFormats(formats -> this.bundleFormats = formats);
        configurator.configureDefaultBundle(bundle -> defaultBundle = bundle);

        configurator.configureValuePipeline(
                cache -> this.values = cache,
                queue -> this.valueQueue = queue);

        configurator.configureBundlePipeline(
                cache -> this.bundles = cache,
                queue -> this.bundleQueue = queue);


        configurator.configureObjectPipeline(
                cache -> this.objects = cache,
                queue -> this.objectQueue = queue);

    }

    private void resetCaches() {
        ResolvedName objectTaskName = new ResolvedName(":", withoutContext());
        ResolvedKey valueTaskName = new ResolvedKey(ResourceKey.key(":"), withoutContext());
        final CountDownLatch bundleLatch = new CountDownLatch(1);
        final CountDownLatch valueLatch = new CountDownLatch(1);
        resetCache("objects", objects, objectTaskName, objectQueue, objectRequests, null, bundleLatch);
        resetCache("bundles", bundles, objectTaskName, bundleQueue, bundleRequests, bundleLatch, valueLatch);
        resetCache("values", values, valueTaskName, valueQueue, valueRequests, valueLatch, null);
    }

    private <K,V> void resetCache(String cacheName,
                                  Cache<K, V> cache,
                                  K resolvedKey,
                                  ExecutorService queue,
                                  Map<K, Future<V>> requests,
                                  CountDownLatch barrier,
                                  CountDownLatch latch) {
        CacheRecord<V> value = cache.putIfAbsent(resolvedKey, initial());
        fetchIfNotInitialized(value, resolvedKey, requests, () -> queue.submit(() -> {
            if (barrier != null) {
                while (barrier.getCount() > 0) {
                    barrier.await();
                }
            }
            cache.clear();
            LOG.debug("Cache {} has been reset", cacheName);
            if (latch != null) {
                latch.countDown();
            }
            return null;
        }));
    }

    private ResolvedName bundleName(ResourceKey key, ResourceResolutionContext context) {
        String bundle = key.getBundle();
        if (bundle == null) {
            return null;
        }
        String objectName = bundle.replace('.', '/');
        ResolvedName name = new ResolvedName(objectName, context);
        return name;
    }

    private CachedValue loadValue(ResolvedKey resolvedKey) {

        ResourceKey bundle = resolvedKey.key().getBundle() != null ? resolvedKey.key() : defaultBundle;

        final ResolvedName bundleName = bundleName(bundle, resolvedKey.context());

        CacheRecord<CachedBundle> value = bundles.putIfAbsent(bundleName, initial());
        fetchIfNotInitialized(value, bundleName, bundleRequests, () -> {
            Future<CachedBundle> future = null;
            for (BundleFormat format : bundleFormats) {
                future = fireRequest(future, bundleQueue,
                        () -> this.loadBundle(format.applyTo(bundleName), format.parser()));
            }
            return future;
        });

        if (value.is(CacheRecord.StateType.EXISTS)) {
            CachedBundle cachedBundle = value.get();
            String valueString = cachedBundle.get(resolvedKey.key().getId());
            return new CachedValue(valueString, cachedBundle.source());

        }

        return new CachedValue(null, null);
    }

    private CachedBundle loadBundle(ResolvedName bundleName, BundleParser bundle) {
        ResourceObject object = loadObjectNonRecursive(bundleName);
        try {
            Map<String, String> bundleMap = object.parsedTo(bundle).asIs();
            return new CachedBundle(object.actualName(), bundleMap);
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
                existingRequests.remove(key);
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

}

