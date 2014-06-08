package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

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
    * @param value
    * @since 2.0
    * 
    */
   void put(ResourceKey key, ResourceResolutionContext context, Object value);

   /**
     *
     * @param key
     * @param locale
     */
    void remove(ResourceKey key, Locale locale);

    /**
    *
    * @param key
    * @param locale
    * @since 2.0
    */
   void remove(ResourceKey key, ResourceResolutionContext context);
}
