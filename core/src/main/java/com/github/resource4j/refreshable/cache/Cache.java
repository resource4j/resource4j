package com.github.resource4j.refreshable.cache;

import com.github.resource4j.refreshable.ResolvedKey;

import java.io.Serializable;

public interface Cache<O> {

	CacheRecord<O> get(ResolvedKey key);

	void put(ResolvedKey key, CacheRecord<O> value);

	CacheRecord<O> putIfAbsent(ResolvedKey key, CacheRecord<O> initial);
}
