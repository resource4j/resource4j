package com.github.resource4j.resources.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class SimpleResourceCache<T> implements ResourceCache<T> {

    private Map<ResourceResolutionContext,Map<ResourceKey,CachedValue<T>>> cachedResources =
            new ConcurrentHashMap<>();

    /**
     * @param context
     * @return
     */
    protected Map<ResourceKey, CachedValue<T>> getcontextResources(ResourceResolutionContext context) {
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
        return contextResources.get(key);
    }

    @Override
    public void put(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> contextResources = getcontextResources(context);
        contextResources.put(key, value);
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
        }
    }

    @Override
    public void clear() {
        cachedResources.clear();
    }

    @Override
    public void putIfAbsent(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> contextResources = getcontextResources(context);
        if (!contextResources.containsKey(key)) {
            contextResources.put(key, value);
        }
    }

}
