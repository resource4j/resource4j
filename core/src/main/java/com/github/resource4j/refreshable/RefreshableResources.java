package com.github.resource4j.refreshable;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.refreshable.cache.*;
import com.github.resource4j.resources.AbstractResources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.values.GenericOptionalString;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.resource4j.refreshable.cache.CacheRecord.initial;
import static com.github.resource4j.resources.context.ResourceResolutionContext.*;
import static java.util.stream.StreamSupport.stream;

/**
 * 1. Push request ("bundle.k1", ctx)
 * 2. Map ("bundle.k1", ctx) -> ("bundle-ctx-A", "bundle-ctx-B")
 * 3. Load "bundle-ctx-A" -> ("bundle-ctx-A:bundle.k1", "bundle-ctx-A:bundle.k2"...)
 * 4. Reduce ("bundle-ctx-A:bundle.k1","bundle-ctx-B:bundle.k1" -> "bundle-ctx-A:bundle.k1")
 * 5.
 */
public class RefreshableResources extends AbstractResources {

    private Cache<CachedValue> values;

    private Cache<CachedObject> objects;

    private List<ResourceObjectProvider> providers;

    private Clock clock = Clock.systemUTC();

    private ReentrantLock requestLock = new ReentrantLock();

    private Map<ResolvedKey, Future<CachedValue>> valueRequests = new HashMap<>();

	private Map<ResolvedKey, Future<CachedObject>> objectRequests = new HashMap<>();

	private ExecutorService valueQueue;

	private ExecutorService objectQueue;

    @Override
	public OptionalString get(ResourceKey key, ResourceResolutionContext context) {
	    ResolvedKey resolvedKey = new ResolvedKey(key, context);
	    return doGet(
			    resolvedKey,
			    values,
			    requestLock,
			    valueRequests,
			    valueQueue,
			    this::loadValue,
			    record -> toOptionalString(resolvedKey, record));
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
		return string;
	}

	private ResourceObject toResourceObject(ResolvedKey resolvedKey, CacheRecord<CachedObject> record) {
		ResourceObject object;
		String name = resolvedKey.key().getBundle();
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
		return object;
	}

	private <V,O extends CachedResult> V doGet(ResolvedKey resolvedKey,
	                                           Cache<O> cache,
	                                           ReentrantLock requestLock,
	                                           Map<ResolvedKey, Future<O>> requests,
	                                           ExecutorService queue,
	                                           Function<ResolvedKey, O> loader,
	                                           Function<CacheRecord<O>,V> mapper) {
		CacheRecord<O> value = cache.putIfAbsent(resolvedKey, initial());
		// Cached value may be updated with loaded results so we use the double-check locking here
		if (value.is(CacheRecord.StateType.PENDING)) {
			requestLock.lock();
			Future<O> future = null;
			try {
				if (value.is(CacheRecord.StateType.PENDING)) {
					// check if request has already been submitted in another thread
					future = requests.get(resolvedKey);
					if (future == null) {
						// not submitted, so we'll create requests for this key and all parent contexts
						LinkedList<ResolvedKey> parentKeys = parentsOf(resolvedKey.context())
								.map(ctx -> new ResolvedKey(resolvedKey.key(), ctx))
								.collect(Collectors.toCollection(LinkedList::new));

						for (Iterator<ResolvedKey> iterator = parentKeys.descendingIterator(); iterator.hasNext(); ) {
							ResolvedKey parentKey = iterator.next();
							LOG.trace("Requested {}", parentKey);
							future = fireRequest(future, queue, () -> loader.apply(parentKey));
							requests.put(parentKey, future);
						}

						LOG.trace("Requested {}", resolvedKey);
						future = fireRequest(future, queue, () -> loader.apply(resolvedKey));
						requests.put(resolvedKey, future);
					}
				}
			} finally {
				requestLock.unlock();
			}

			// future can be null if by second check above the value has already been loaded
			// if it's not null, then we are expecting new result and need to wait
			if (future != null) {
				try {
					O result = future.get();
					requestLock.lock();
					value.store(result, clock::millis);
					LOG.trace("Cached result {}", result);
				} catch (InterruptedException | ExecutionException e) {
					requestLock.lock();
					value.fail(e);
				} finally {
					requestLock.unlock();
				}
			}
		}
		return mapper.apply(value);
	}

    private <O extends CachedResult> Future<O> fireRequest(Future<O> parent,
		ExecutorService queue,
        Callable<O> loader) {
        Future<O> future = queue.submit(loader);
	    return new FallbackFuture<>(future, parent, CachedResult::exists);
    }

	private CachedValue loadValue(ResolvedKey key) {
		return null;
	}

	private CachedObject loadObject(ResolvedKey key) {
		return null;
	}

	@Override
	public ResourceObject contentOf(String name, ResourceResolutionContext context) {
		ResolvedKey resolvedKey = new ResolvedKey(ResourceKey.bundle(name), context);
		return doGet(
				resolvedKey,
				objects,
				requestLock,
				objectRequests,
				objectQueue,
				this::loadObject,
				record -> toResourceObject(resolvedKey, record));
	}

	public void setObjectProviders(List<ResourceObjectProvider> providers) {
        this.providers = providers;
    }

}

