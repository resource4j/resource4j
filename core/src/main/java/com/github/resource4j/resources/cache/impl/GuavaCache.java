package com.github.resource4j.resources.cache.impl;

import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CacheRecord;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class GuavaCache<K, V> implements Cache<K, V> {

    private com.google.common.cache.Cache<K, CacheRecord<V>> guavaCache;

    public static <K,V> Supplier<Cache<K, V>> guava(CacheBuilder<Object, Object> cacheBuilder) {
        return () -> new GuavaCache<>(cacheBuilder.build());
    }

    public GuavaCache(com.google.common.cache.Cache<K, CacheRecord<V>> guavaCache) {
        this.guavaCache = guavaCache;
    }

    @Override
    public CacheRecord<V> get(K key) {
        return guavaCache.getIfPresent(key);
    }

    @Override
    public CacheRecord<V> putIfAbsent(K key, CacheRecord<V> initial) {
        try {
            return guavaCache.get(key, () -> initial);
        } catch (ExecutionException e) {
            throw new UnsupportedOperationException("Guava Cache failed to initialize data record for " + key, e);
        }
    }

    @Override
    public void clear() {
        guavaCache.invalidateAll();
    }
}
