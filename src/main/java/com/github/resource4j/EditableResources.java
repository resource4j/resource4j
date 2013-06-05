package com.github.resource4j;

import java.util.Locale;

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
