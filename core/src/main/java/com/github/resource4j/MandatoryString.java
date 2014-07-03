package com.github.resource4j;

import java.text.Format;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface MandatoryString extends MandatoryValue<String>, ResourceString {

    @Override
    <T> MandatoryValue<T> ofType(Class<T> type);
    
    /**
     * @since 2.0
     */
    @Override
    <T> ResourceValue<T> ofType(Class<T> type, String format);
    
    /**
     * @since 2.0
     */
    @Override
    <T> ResourceValue<T> ofType(Class<T> type, Format format);    

    <T> T as(Class<T> type);

    <T> T as(Class<T> type, String format);

    <T> T as(Class<T> type, Format format);

    /**
     * Formats the given arguments using resource value and {@link MessageFormat} and returns the result string.
     * Implementation of this method assumed to be thread-safe.
     * @param arguments the arguments to format
     * @return formatted string
     * @throws IllegalArgumentException as specified in {@link MessageFormat#format(String, Object...)}
     */
    String asFormatted(Object... arguments);
}
