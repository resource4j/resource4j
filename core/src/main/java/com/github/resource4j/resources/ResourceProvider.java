package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.resources.context.ResourceResolutionContext;

/**
 * Resource provider is a component providing convenient access to the values in some specific resource bundle.
 * 
 * @author Ivan Gammel
 * @since 1.0
 */
public interface ResourceProvider {

    /**
     * Convenience method for obtaining value in resolution context that consists of single {@link Locale} object. 
     * @param name identifier of the value in managed bundle
     * @param locale the resolution context
     * @return the value defined in given resolution context
     */
    OptionalString get(String name, Locale locale);

    /**
     * 
     * @param name identifier of the value in managed bundle 
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return value as optional string
     * @see ResourceKey
     * @see ResourceResolutionContext
     * @since 2.0
     */
    OptionalString get(String name, ResourceResolutionContext context);

    /**
     * Convenience method for obtaining resource file in resolution context that consists of single {@link Locale} object. 
     * @param name name of resource file to look up
     * @param locale the resolution context
     * @return reference to the resource file
     * @since 1.1
     */
    ResourceObject contentOf(String name, Locale locale);

    /**
     * Convenience method for obtaining reference to a resource file with given name in given resolution context: 
     * implementations of this method simply delegate execution to underlying {@link Resources} implementation. 
     * The provider bundle is not affecting the lookup process.
     * @param name name of resource file to look up
     * @param context the context for searching the resource file as defined in documentation to {@link ResourceResolutionContext}.
     * @return reference to the resource file 
     * @see ResourceKey 
     * @since 2.0
     */
    ResourceObject contentOf(String name, ResourceResolutionContext context);

}
