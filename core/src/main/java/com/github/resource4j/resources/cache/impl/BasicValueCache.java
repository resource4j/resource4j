package com.github.resource4j.resources.cache.impl;

import com.github.resource4j.resources.cache.Cache;
import com.github.resource4j.resources.cache.CacheRecord;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasicValueCache<K, V> implements Cache<K, V> {

    private ConcurrentMap<K, CacheRecord<V>> values = new ConcurrentHashMap<>();

    @Override
    public CacheRecord<V> putIfAbsent(K key, CacheRecord<V> value) {
        CacheRecord<V> result = values.putIfAbsent(key, value);
        return result == null ? value : result;
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public CacheRecord<V> get(K key) {
        return values.get(key);
    }

}
