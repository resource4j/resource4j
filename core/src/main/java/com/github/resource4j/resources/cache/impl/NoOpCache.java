package com.github.resource4j.resources.cache.impl;

import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CacheRecord;

public class NoOpCache<K, V> implements Cache<K, V> {
    @Override
    public CacheRecord<V> get(K key) {
        return null;
    }
    @Override
    public CacheRecord<V> putIfAbsent(K key, CacheRecord<V> initial) {
        return initial;
    }
    @Override
    public void clear() {
    }
}
