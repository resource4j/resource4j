package com.github.resource4j.resources.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class SimpleResourceCache<T> implements ResourceCache<T> {

	private Logger log = LoggerFactory.getLogger(SimpleResourceCache.class); 
	
	private ConcurrentHashMap<SimpleResourceCacheKey,CachedValue<T>> cachedResources =
            new ConcurrentHashMap<>();


    @Override
    public CachedValue<T> get(ResourceKey key, ResourceResolutionContext context) {
        CachedValue<T> value = cachedResources.get(new SimpleResourceCacheKey(context, key));
        if (value != null) {
            log.trace("Cache hit: {}[{}]={}", key, context, value);
        } else {
            log.trace("Cache miss: {}[{}]", key, context, value);
        }
		return value;
    }

    @Override
    public void put(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
        cachedResources.put(new SimpleResourceCacheKey(context, key), value);
        log.trace("Cached: {}[{}]={}", key, context, value);
        
    }

    @Override
    public void evict(ResourceKey key, ResourceResolutionContext context) {
    	CachedValue<T> value = cachedResources.remove(new SimpleResourceCacheKey(context, key));
    	if (value != null) {
    		log.trace("Evicted {}[{}]", key, context);
    	}
    }

    @Override
    public void clear() {
        cachedResources.clear();
    }

    @Override
    public void putIfAbsent(ResourceKey key, ResourceResolutionContext context, CachedValue<T> value) {
    	CachedValue<T> prevValue = cachedResources.putIfAbsent(new SimpleResourceCacheKey(context, key), value);
        if (prevValue == null) {
            log.trace("Cached: {}[{}]={}", key, context, value);
        } 
    }

}
