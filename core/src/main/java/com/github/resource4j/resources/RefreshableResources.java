package com.github.resource4j.resources;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceException;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.parsers.BundleParser;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.objects.providers.FilteringResourceObjectProvider;
import com.github.resource4j.objects.providers.ResourceObjectProviderAdapter;
import com.github.resource4j.objects.providers.ResourceValueProvider;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.resources.cache.*;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.impl.FallbackFuture;
import com.github.resource4j.resources.impl.ResolvedKey;
import com.github.resource4j.resources.impl.ResolvedName;
import com.github.resource4j.resources.processors.CyclicReferenceException;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;
import com.github.resource4j.values.GenericOptionalString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.cache.CacheRecord.initial;
import static com.github.resource4j.resources.context.ResourceResolutionContext.parentsOf;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toCollection;

public class RefreshableResources implements Resources {

    private static final int DEFAULT_MAX_DEPTH = 20;

    private static final Logger LOG = LoggerFactory.getLogger(RefreshableResources.class);

    private Cache<ResolvedKey, CachedValue> values;

    private Cache<ResolvedName, CachedBundle> bundles;

    private Cache<ResolvedName, ResourceObject> objects;

    private List<ResourceObjectProvider> providers;

    private Clock clock = Clock.systemUTC();

    private ReentrantLock requestLock = new ReentrantLock();

    private Map<ResolvedKey, Future<CachedValue>> valueRequests = new HashMap<>();

    private Map<ResolvedName, Future<CachedBundle>> bundleRequests = new HashMap<>();

	private Map<ResolvedName, Future<ResourceObject>> objectRequests = new HashMap<>();

	private ExecutorService valueQueue;

    private ExecutorService bundleQueue;

	private ExecutorService objectQueue;

    private List<BundleFormat> bundleFormats;

    private ResourceKey defaultBundle;

    private int maxDepth = DEFAULT_MAX_DEPTH;

    private ThreadLocal<Integer> cycleDetector = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    private ResourceValuePostProcessor valuePostProcessor;

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
            this.providers.stream()
                    .map(RefreshableResources::unwrap)
                    .flatMap(Collection::stream)
                    .filter(provider -> provider instanceof ResourceObjectRepository)
                    .distinct()
                    .forEach(provider -> {
                        ResourceObjectRepository repository = (ResourceObjectRepository) provider;
                        repository.addListener(listener);
                    });
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

        configurator.configurePostProcessing(p -> this.valuePostProcessor = p);

    }

    private static List<ResourceObjectProvider> unwrap(ResourceObjectProvider provider) {
        return provider instanceof ResourceObjectProviderAdapter
                ? ((ResourceObjectProviderAdapter) provider).unwrap()
                : singletonList(provider);
    }

    private void resetCaches() {
        ResolvedName objectTaskName = new ResolvedName(":", withoutContext());
        ResolvedKey valueTaskName = new ResolvedKey(ResourceKey.key(":reset:"), withoutContext());
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
        CacheRecord<V> value = cache.putIfAbsent(resolvedKey, initialRecord());
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
        }), Function.identity());
    }

    private ResolvedName bundleName(ResourceKey key, ResourceResolutionContext context) {
        String objectName = key.objectName();
        ResolvedName name = new ResolvedName(objectName, context);
        return name;
    }

    private CachedValue loadValueFromBundle(ResolvedKey resolvedKey) {
        ResourceKey bundle = resolvedKey.key().getBundle() != null ? resolvedKey.key() : defaultBundle;

        final ResolvedName bundleName = bundleName(bundle, resolvedKey.context());

        CacheRecord<CachedBundle> value = bundles.putIfAbsent(bundleName, initialRecord());
        fetchIfNotInitialized(value, bundleName, bundleRequests, () -> {
            Future<CachedBundle> future = null;
            for (BundleFormat format : bundleFormats) {
                future = fireRequest(future, bundleQueue,
                        () -> this.loadBundle(format.applyTo(bundleName), format.parser()),
                        CachedResult::exists);
            }
            return future;
        }, Function.identity());

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
        CacheRecord<ResourceObject> value = objects.putIfAbsent(resolvedName, initialRecord());
        fetchIfNotInitialized(value, resolvedName,
                objectRequests,
                () -> fireAllRequests(resolvedName, null),
                Function.identity());
        return toResourceObject(resolvedName, value);
    }

    private <K, O> void cacheObject(K key, Future<O> future, CacheRecord<O> value, Function<O,O> process) {
        try {
            O result = future.get();
            result = process.apply(result);
            requestLock.lock();
            try {
                value.store(result, clock::millis);
                LOG.trace("Cached {} -> {}", key, result);
            } finally {
                requestLock.unlock();
            }
        } catch (InterruptedException | ExecutionException | ResourceException e) {
            requestLock.lock();
            try {
                if (value.state() == CacheRecord.StateType.PENDING) {
                    Throwable cause = e instanceof ExecutionException ? e.getCause() : e;
                    value.fail(cause);
                    LOG.trace("Cached error {} -> {}", key, cause.getClass().getSimpleName());
                }
            } finally {
                requestLock.unlock();
            }
            if (e instanceof CyclicReferenceException) {
                throw (CyclicReferenceException) e;
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
        try {
            return doGet(resolvedKey);
        } catch (CyclicReferenceException e) {
            return new GenericOptionalString(null, key, null, e);
        }
	}

    private OptionalString doGet(ResolvedKey resolvedKey) {
        int depth = cycleDetector.get();
        if (depth < maxDepth) {
            cycleDetector.set(depth + 1);
        } else {
            throw new CyclicReferenceException();
        }

        CacheRecord<CachedValue> value = values.putIfAbsent(resolvedKey, initialRecord());
        // Cached value may be updated with loaded results so we use the double-check locking here
        fetchIfNotInitialized(value, resolvedKey, valueRequests, () -> {
            Future<CachedValue> future = null;
            // not submitted, so we'll create requests for this key and all parent contexts
            LinkedList<ResolvedKey> parentKeys = parentsOf(resolvedKey.context())
                    .map(ctx -> new ResolvedKey(resolvedKey.key(), ctx))
                    .collect(toCollection(LinkedList::new));

            for (Iterator<ResolvedKey> iterator = parentKeys.descendingIterator(); iterator.hasNext(); ) {
                ResolvedKey parentKey = iterator.next();
                future = fireRequest(future, valueQueue,
                        () -> this.loadValueFromBundle(parentKey),
                        CachedResult::exists);
                valueRequests.put(parentKey, future);
            }
            future = fireRequest(future, valueQueue,
                    () -> this.loadValueFromBundle(resolvedKey),
                    CachedResult::exists);
            valueRequests.put(resolvedKey, future);

            for (Iterator<ResolvedKey> iterator = parentKeys.descendingIterator(); iterator.hasNext(); ) {
                ResolvedKey parentKey = iterator.next();
                future = loadSingleValue(future, parentKey);
            }
            future = loadSingleValue(future, resolvedKey);

            return future;
        }, val -> {
            if (valuePostProcessor == null) return val;
            String string = val.value();
            if (string == null) return val;
            string = valuePostProcessor.process(id -> doGet(resolvedKey.relative(id)).asIs(), string);
            return new CachedValue(string, val.source());
        });
        return toOptionalString(resolvedKey, value);
    }

    private Future<CachedValue> loadSingleValue(Future<CachedValue> future, ResolvedKey parentKey) {
        for (ResourceObjectProvider provider : providers) {
            if (provider instanceof ResourceValueProvider) {
                future = fireRequest(future, valueQueue,
                        () -> this.loadSingleValue((ResourceValueProvider) provider, parentKey),
                        CachedResult::exists);
                valueRequests.put(parentKey, future);
            }
        }
        return future;
    }

    private CachedValue loadSingleValue(ResourceValueProvider provider, ResolvedKey parentKey) {
        OptionalString value = provider.get(parentKey.key(), parentKey.context());
        return new CachedValue(value.asIs(), value.resolvedSource());
    }

    protected <K,O> void fetchIfNotInitialized(CacheRecord<O> value, K key,
                                               Map<K, Future<O>> existingRequests,
                                               Supplier<Future<O>> fetchTaskSupplier,
                                               Function<O,O> processor) {
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
                cacheObject(key, future, value, processor);
                existingRequests.remove(key);
            }
        }
    }

	private ResourceObject toResourceObject(ResolvedName resolvedName, CacheRecord<ResourceObject> record) {
		ResourceObject object;
		String name = resolvedName.name();
		switch (record.state()) {
			case EXISTS:
				object = record.get();
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

    private <O> Future<O> fireRequest(Future<O> parent,
                                      ExecutorService queue,
                                      Callable<O> loader, Predicate<O> validator) {
        Future<O> future = queue.submit(loader);
	    return parent == null ? future : new FallbackFuture<>(future, parent, validator);
    }

	private ResourceObject loadObject(ResourceObjectProvider provider, ResolvedName resolvedName) {
        try {
            long millis = clock.millis();
            ResourceObject object = provider.get(resolvedName.name(), resolvedName.context());
            LOG.trace("Request complete: {} loaded in {} ms from {}", resolvedName, clock.millis() - millis, provider);
            return object;
        } catch (ResourceObjectAccessException e) {
            return null;
        }
	}

	@Override
	public ResourceObject contentOf(String name, ResourceResolutionContext context) {
		ResolvedName resolvedKey = new ResolvedName(name, context);
		CacheRecord<ResourceObject> value = objects.putIfAbsent(resolvedKey, initialRecord());
        fetchIfNotInitialized(value, resolvedKey, objectRequests, () -> {
            Future<ResourceObject> future = null;
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
        }, Function.identity());
		return toResourceObject(resolvedKey, value);
	}

    private <V> CacheRecord<V> initialRecord() {
        return initial();
    }

    protected Deque<ResourceObjectProvider> getSortedProviders() {
        return this.providers.stream()
                        .collect(toCollection(ArrayDeque::new));
    }

    protected Future<ResourceObject> fireAllRequests(ResolvedName resolvedKey, Future<ResourceObject> future) {
        Deque<ResourceObjectProvider> providers = getSortedProviders();
        for (Iterator<ResourceObjectProvider> pIterator = providers.descendingIterator(); pIterator.hasNext();) {
            ResourceObjectProvider provider = pIterator.next();
            future = fireRequest(future, objectQueue,
                    () -> this.loadObject(provider, resolvedKey),
                    Objects::nonNull);
        }
        objectRequests.put(resolvedKey, future);
        return future;
    }

}

