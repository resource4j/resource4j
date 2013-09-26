package com.github.resource4j.resources.cache;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.resource4j.ResourceKey;

public class SimpleResourceCache<T> implements ResourceCache<T> {

    private Map<Locale,Map<ResourceKey,CachedValue<T>>> cachedResources =
            new ConcurrentHashMap<Locale, Map<ResourceKey,CachedValue<T>>>();

    /**
     * @param locale
     * @return
     */
    protected Map<ResourceKey, CachedValue<T>> getLocaleResources(Locale locale) {
        Map<ResourceKey, CachedValue<T>> localeResources = cachedResources.get(locale);
        if (localeResources == null) {
            localeResources = new ConcurrentHashMap<ResourceKey, CachedValue<T>>();
            cachedResources.put(locale, localeResources);
        }
        return localeResources;
    }

    @Override
    public CachedValue<T> get(ResourceKey key, Locale locale) {
        Map<ResourceKey, CachedValue<T>> localeResources = cachedResources.get(locale);
        if (localeResources == null) return null;
        return localeResources.get(key);
    }

    @Override
    public void put(ResourceKey key, Locale locale, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> localeResources = getLocaleResources(locale);
        localeResources.put(key, value);
    }

    @Override
    public void evict(ResourceKey key, Locale locale) {
        Map<ResourceKey, CachedValue<T>> localeResources = cachedResources.get(locale);
        if (localeResources == null) {
            return;
        }
        localeResources.remove(key);
        if (localeResources.isEmpty()) {
            cachedResources.remove(locale);
        }
    }

    @Override
    public void clear() {
        cachedResources.clear();
    }

    @Override
    public void putIfAbsent(ResourceKey key, Locale locale, CachedValue<T> value) {
        Map<ResourceKey, CachedValue<T>> localeResources = getLocaleResources(locale);
        if (!localeResources.containsKey(key)) {
            localeResources.put(key, value);
        }
    }

}
