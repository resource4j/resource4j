package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

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
     * @param key 
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return value as optional string
     * @see ResourceKey
     * @see ResourceResolutionContext
     * @since 2.0
     */
    OptionalString get(String name, ResourceResolutionContext context);

    /**
     *
     * @param name
     * @param locale
     * @return
     * @since 1.1
     */
    ResourceFile contentOf(String name, Locale locale);

    /**
     * 
     * @param name
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return reference to resource file to work with
     * @see ResourceKey 
     * @since 2.0
     */
    ResourceFile contentOf(String name, ResourceResolutionContext context);

}
