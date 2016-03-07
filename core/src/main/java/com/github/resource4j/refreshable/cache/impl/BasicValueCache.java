package com.github.resource4j.refreshable.cache.impl;

import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.ResolvedKey;
import com.github.resource4j.refreshable.cache.CacheRecord;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasicValueCache<K, V> implements Cache<K, V> {

    private ConcurrentMap<K, CacheRecord<V>> values = new ConcurrentHashMap<>();

    @Override
    public CacheRecord<V> putIfAbsent(K key, CacheRecord<V> value) {
        CacheRecord<V> result = value;
        values.putIfAbsent(key, value);
        return result;
    }

    @Override
    public CacheRecord<V> get(K key) {
        return values.get(key);
    }

    @Override
    public void put(K key, CacheRecord<V> value) {
        values.put(key, value);
    }

}
