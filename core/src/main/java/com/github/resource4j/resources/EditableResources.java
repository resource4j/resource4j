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
    void put(ResourceKey key, Locale locale, String value);

    /**
     * 
     * @param name
     * @param locale
     * @param content
     * @since 2.0
     */
    void put(String name, Locale locale, byte[] content);
    
    /**
    *
    * @param key
    * @param locale
    * @param value
    * @since 2.0
    * 
    */
   void put(ResourceKey key, ResourceResolutionContext context, String value);

   /**
    * 
    * @param name
    * @param context
    * @param content
    */
   void put(String name, ResourceResolutionContext context, byte[] content);
   
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
