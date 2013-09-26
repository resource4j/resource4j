package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.ResourceKey;

/**
 *
 * @author Ivan Gammel
 */
public interface EditableResources extends Resources {

    /**
     *
     * @param key
     * @param locale
     * @param value
     */
    void put(ResourceKey key, Locale locale, Object value);

    /**
     *
     * @param key
     * @param locale
     */
    void remove(ResourceKey key, Locale locale);

}
