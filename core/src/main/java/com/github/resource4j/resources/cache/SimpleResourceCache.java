package com.github.resource4j.resources.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class SimpleResourceCache<T> implements ResourceCache<T> {

	private Logger log = LoggerFactory.getLogger(SimpleResourceCache.class); 
	
    private Map<ResourceResolutionContext,Map<ResourceKey,CachedValue<T>>> cachedResources =
            new ConcurrentHashMap<>();

    /**
     * @param context
     * @return
     */
    protected Map<ResourceKey, CachedValue<T>> getContextResources(ResourceResolutionContext context) {
        Map<ResourceKey, CachedValue<T>> contextResources = cachedResources.get(context);
        if (contextResources == null) {
            contextResources = new ConcurrentHashMap<ResourceKey, CachedValue<T>>();
            cachedResources.put(context, contextResources);
        }
        return contextResources;
    }

    @Override
    public CachedValue<T> get(ResourceKey key, ResourceResolutionContext context) {
        Map<ResourceKey, CachedValue<T>> contextResources = cachedResources.get(context);
        if (contextResources == null) return null;
        CachedValue<T> value = contextResources.get(key);
        log.trace("Found resource {} in context {}: {}", key, context, value);
		return value;
    }

    @Override
    public void put(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> contextResources = getContextResources(context);
        contextResources.put(key, value);
        log.trace("Cached resource {} in context {}: {}", key, context, value);
        
    }

    @Override
    public void evict(ResourceKey key, ResourceResolutionContext context) {
        Map<ResourceKey, CachedValue<T>> contextResources = cachedResources.get(context);
        if (contextResources == null) {
            return;
        }
        contextResources.remove(key);
        if (contextResources.isEmpty()) {
            cachedResources.remove(context);
            log.trace("Resource {} in context {} evicted from cache.", key, context);
        }
    }

    @Override
    public void clear() {
        cachedResources.clear();
    }

    @Override
    public void putIfAbsent(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> contextResources = getContextResources(context);
        if (!contextResources.containsKey(key)) {
            contextResources.put(key, value);
            log.trace("Cached resource {} in context {}: {}", key, context);
        }
    }

}
