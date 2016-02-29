package com.github.resource4j.refreshable.cache.impl;

import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.ResolvedKey;
import com.github.resource4j.refreshable.cache.CacheRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasicValueCache<O> implements Cache<O> {
    @Override
    public CacheRecord<O> putIfAbsent(ResolvedKey key, CacheRecord<O> value) {
        return values.putIfAbsent(key, value);
    }

    private ConcurrentMap<ResolvedKey, CacheRecord<O>> values = new ConcurrentHashMap<>();

    @Override
    public CacheRecord<O> get(ResolvedKey key) {
        return values.get(key);
    }

    @Override
    public void put(ResolvedKey key, CacheRecord<O> value) {
        values.put(key, value);
    }

}
