package com.github.resource4j.resources.cache;

import com.github.resource4j.resources.cache.impl.BasicValueCache;
import com.github.resource4j.resources.cache.impl.NoOpCache;

import java.util.function.Supplier;

public final class Caches {

    public static <K,V> Supplier<Cache<K,V>> always() {
        return BasicValueCache::new;
    }

    public static <K,V> Supplier<Cache<K,V>> never() {
        return NoOpCache::new;
    }

}
