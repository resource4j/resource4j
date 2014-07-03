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
public interface Resources {

    /**
     *
     * @param key
     * @return
     */
    ResourceProvider forKey(ResourceKey key);

    /**
     * Returns value with no resolution context specified.
     * @since 2.0
     */
    OptionalString get(ResourceKey key);
    
    /**
     *
     * @param key
     * @param locale
     * @return value as optional string
     */
    OptionalString get(ResourceKey key, Locale locale);

    /**
     * 
     * @param key 
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return value as optional string
     * @see ResourceKey
     * @see ResourceResolutionContext
     * @since 2.0
     */
    OptionalString get(ResourceKey key, ResourceResolutionContext context);
    
    /**
     *
     * @param name
     * @param locale
     * @return refe
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
    
    /**
     * @param name
     * @return
     * @since 2.0
     */
    ResourceFile contentOf(String name);

}
