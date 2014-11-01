package com.github.resource4j;

import com.github.resource4j.util.TypeCastException;

/**
 * Specialized representation of {@link OptionalValue} for values of type {@link String}.
 * @author Ivan Gammel
 * @since 1.0
 */
public interface OptionalString extends OptionalValue<String>, ResourceString {

    @Override
    <T> OptionalValue<T> ofType(Class<T> type) throws TypeCastException;

    @Override
    MandatoryString or(String defaultValue) throws IllegalArgumentException;

    @Override
    String orDefault(String defaultValue) throws IllegalArgumentException;

    @Override
    MandatoryString notNull() throws MissingValueException;

}
