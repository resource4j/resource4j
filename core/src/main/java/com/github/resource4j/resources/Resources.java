package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
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
     * @param name
     * @param locale
     * @return
     * @since 1.1
     */
    ResourceFile contentOf(String name, Locale locale);


}
