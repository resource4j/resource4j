package com.github.resource4j;

import java.util.Locale;

import com.github.resource4j.files.ResourceFile;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface ResourceProvider {

    /**
     *
     * @param name
     * @param locale
     * @return
     */
    OptionalString get(String name, Locale locale);

    /**
     *
     * @param name
     * @param locale
     * @return
     * @since 1.1
     */
    ResourceFile contentOf(String name, Locale locale);

}
