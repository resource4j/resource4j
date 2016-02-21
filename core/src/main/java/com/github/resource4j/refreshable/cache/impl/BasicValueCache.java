package com.github.resource4j.refreshable.cache.impl;

import com.github.resource4j.refreshable.cache.Cache;
import com.github.resource4j.refreshable.ResolvedKey;
import com.github.resource4j.refreshable.cache.CacheRecord;

import java.util.HashMap;
import java.util.Map;

public class BasicValueCache<O> implements Cache<O> {

    private Map<ResolvedKey, CacheRecord<O>> values = new HashMap<>();

    @Override
    public CacheRecord<O> get(ResolvedKey key) {
        return values.get(key);
    }

    @Override
    public void put(ResolvedKey key, CacheRecord<O> value) {
        values.put(key, value);
    }

}
