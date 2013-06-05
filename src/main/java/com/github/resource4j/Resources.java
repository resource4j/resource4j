package com.github.resource4j;

import java.util.Locale;

import com.github.resource4j.files.ResourceFile;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface Resources {

    /**
     *
     * @param key
     * @return
     */
    ResourceProvider forKey(ResourceKey key);

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    OptionalString get(ResourceKey key, Locale locale);

    /**
     *
     * @param key
     * @param locale
     * @return
     * @since 1.1
     */
    ResourceFile contentOf(ResourceKey key, Locale locale);


}
