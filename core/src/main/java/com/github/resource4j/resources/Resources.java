package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

/**
 * The core class of Resource4J API that provides access to resource values. 
 * @author Ivan Gammel
 * @since 1.0
 */
public interface Resources {

    /**
     * 
     * @param bundle the bundle key
     * @return resource provider that uses given bundle key
     */
    ResourceProvider forKey(ResourceKey bundle);

    /**
     * Returns value with no resolution context specified.
     * @param key the key that identifies value
     * @return resolved value as optional string
     * @since 2.0
     */
    OptionalString get(ResourceKey key);
    
    /**
     *
     * @param key the key that identifies the value
     * @param locale resolution context defined as locale
     * @return resolved value as optional string
     * @since 1.0
     */
    OptionalString get(ResourceKey key, Locale locale);

    /**
     * 
     * @param key the key that identifies the value
     * @param context the context for searching the resource file as defined by {@link ResourceResolutionContext}.
     * @return value as optional string
     * @see ResourceKey
     * @see ResourceResolutionContext
     * @since 2.0
     */
    OptionalString get(ResourceKey key, ResourceResolutionContext context);
    
    /**
     * Resolves file name in given locale as a resolution context and returns the reference to the file.
     * @param name the file name
     * @param locale the locale to be used as resolution context
     * @return reference to the resolved file
     * @since 1.1
     */
    ResourceFile contentOf(String name, Locale locale);

    /**
     * Resolves file name in given resolution context and returns the reference to the file.
     * @param name the name of the file
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return reference to resource file to work with
     * @see ResourceKey 
     * @since 2.0
     */
    ResourceFile contentOf(String name, ResourceResolutionContext context);
    
    /**
     * Resolves file name without any resolution context.
     * @param name the name of the file to load
     * @return reference to the file for content loading and parsing
     * @since 2.0
     */
    ResourceFile contentOf(String name);

}
