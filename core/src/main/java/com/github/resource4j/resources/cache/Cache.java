package com.github.resource4j.resources.cache;

public interface Cache<K, V> {

	CacheRecord<V> get(K key);

	CacheRecord<V> putIfAbsent(K key, CacheRecord<V> initial);

	void clear();

}
