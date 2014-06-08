package com.github.resource4j.resources.cache;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceCache<V> {

    CachedValue<V> get(ResourceKey key, ResourceResolutionContext context);

    void put(ResourceKey key, ResourceResolutionContext context, CachedValue<V> value);

    void putIfAbsent(ResourceKey key, ResourceResolutionContext context, CachedValue<V> value);

    void evict(ResourceKey key, ResourceResolutionContext context);

    void clear();

}
