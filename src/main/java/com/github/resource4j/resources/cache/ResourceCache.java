package com.github.resource4j.resources.cache;

import java.util.Locale;

import com.github.resource4j.ResourceKey;

public interface ResourceCache<V> {

    CachedValue<V> get(ResourceKey key, Locale locale);

    void put(ResourceKey key, Locale locale, CachedValue<V> value);

    void putIfAbsent(ResourceKey key, Locale locale, CachedValue<V> value);

    void evict(ResourceKey key, Locale locale);

    void clear();

}
