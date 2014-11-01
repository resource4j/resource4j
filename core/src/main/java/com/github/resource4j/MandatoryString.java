package com.github.resource4j;

import java.text.Format;
import java.text.MessageFormat;

import com.github.resource4j.util.TypeCastException;

/**
 * Specialized representation of {@link MandatoryValue} for values of type String.
 * @author Ivan Gammel
 * @since 1.0
 */
public interface MandatoryString extends MandatoryValue<String>, ResourceString {

    @Override
    <T> MandatoryValue<T> ofType(Class<T> type) throws TypeCastException;
    
    @Override
    <T> MandatoryValue<T> ofType(Class<T> type, String format) throws TypeCastException;

    @Override
    <T> MandatoryValue<T> ofType(Class<T> type, Format format) throws TypeCastException;    

    /**
     * Performs conversion of the contained value to given type and returns result.
     * @param type class object for the expected type of value
     * @param <T> the expected type of value
     * @return converted value
     * @see #ofType(Class)
     */
    <T> T as(Class<T> type);

    /**
     * Performs conversion of the contained value to given type using given formatting pattern and returns result.
     * @param type class object for the expected type of value
     * @param format the formatting pattern
     * @param <T> the expected type of value
     * @return converted value
     * @see #ofType(Class, String)
     */
    <T> T as(Class<T> type, String format);

    /**
     * Performs conversion of the contained value to given type using given format and returns result.
     * @param type class object for the expected type of value
     * @param format the formatter object
     * @param <T> the expected type of value
     * @return converted value
     * @see #ofType(Class, Format)
     */
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
