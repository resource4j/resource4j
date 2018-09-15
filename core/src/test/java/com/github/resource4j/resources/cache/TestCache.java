package com.github.resource4j.resources.cache;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TestCache<K, V> implements Cache<K, V> {

    private Cache<K, V> cache;

    private Listener<? super K> listener;

    public static <K, V> Supplier<Cache<K, V>> test(Supplier<? extends Cache<K,V>> supplier,
                                                    Listener<? super K> listener) {
        return () -> new TestCache<>(supplier.get(), listener);
    }

    public TestCache(Cache<K, V> cache, Listener<? super K> listener) {
        this.cache = cache;
        this.listener = listener;
    }

    @Override
    public CacheRecord<V> get(K key) {
        return cache.get(key);
    }

    @Override
    public CacheRecord<V> putIfAbsent(K key, CacheRecord<V> initial) {
        CacheRecord<V> record = cache.putIfAbsent(key, initial);
        if (record.is(CacheRecord.StateType.PENDING)) {
            listener.miss(key);
        } else {
            listener.hit(key);
        }
        return record;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    public interface Listener<K> {
        void miss(K key);
        void hit(K key);
    }

    public static class Counter implements Listener<Object> {

        private AtomicInteger misses = new AtomicInteger(0);
        private AtomicInteger hits = new AtomicInteger(0);

        @Override
        public void miss(Object key) {
            misses.incrementAndGet();
        }

        @Override
        public void hit(Object key) {
            hits.incrementAndGet();
        }

        public int misses() {
            return this.misses.get();
        }

        public int hits() {
            return this.hits.get();
        }

        public void reset() {
            this.misses.set(0);
            this.hits.set(0);
        }
    }

}
