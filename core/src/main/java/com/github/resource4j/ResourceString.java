package com.github.resource4j;

import java.text.Format;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface ResourceString extends ResourceValue<String> {

    <T> ResourceValue<T> ofType(Class<T> type);
    
    /**
     * @since 2.0
     */
    <T> ResourceValue<T> ofType(Class<T> type, String format);
    
    /**
     * @since 2.0
     */
    <T> ResourceValue<T> ofType(Class<T> type, Format format);


}
