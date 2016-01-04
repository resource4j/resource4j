package com.github.resource4j.resources;

import java.util.Locale;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

/**
 * Base interface for resources that can be modified in runtime
 * @author Ivan Gammel
 */
public interface EditableResources extends Resources {

    /**
     * Creates or replaces existing resource value in given locale as a resolution context.
     * @param key the key that identifies the value
     * @param locale resolution context
     * @param value the value to store
     */
    void put(ResourceKey key, Locale locale, String value);

    /**
     * Creates or replaces existing resource file in given locale as a resolution context
     * @param name name of the file
     * @param locale resolution context
     * @param content the file data to store
     * @since 2.0
     */
    void put(String name, Locale locale, byte[] content);
    
    /**
     * Creates or replaces existing resource value in given resolution context.
     * @param key the key that identifies the value
     * @param context resolution context
     * @param value the value to store
     */
   void put(ResourceKey key, ResourceResolutionContext context, String value);

   /**
     * Creates or replaces existing resource file in given resolution context
     * @param name name of the file
     * @param context resolution context
     * @param content the file data to store
    */
   void put(String name, ResourceResolutionContext context, byte[] content);
   
   /**
     * Removes value or resource file identified by given key from resolution context specified by given locale
     * @param key the key identifying the value or resource file
     * @param locale resolution context
     */
    void remove(ResourceKey key, Locale locale);

    /**
    * Removes value or resource file identified by given key from given resolution context
    * @param key the key identifying the value or resource file
    * @param context resolution context
    * @since 2.0
    */
   void remove(ResourceKey key, ResourceResolutionContext context);
}
