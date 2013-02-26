package com.github.resource4j;

import java.text.Format;
import java.text.MessageFormat;

public interface ResourceString extends ResourceValue<String> {

    <T> ResourceValue<T> ofType(Class<T> type);

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
