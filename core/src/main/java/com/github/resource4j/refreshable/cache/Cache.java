package com.github.resource4j.refreshable.cache;

public interface Cache<K, V> {

	CacheRecord<V> get(K key);

	void put(K key, CacheRecord<V> value);

	CacheRecord<V> putIfAbsent(K key, CacheRecord<V> initial);
}
