package com.github.resource4j;

/**
 * Specialized representation of {@link OptionalValue} for values of type {@link String}.
 * @author Ivan Gammel
 * @since 1.0
 */
public interface OptionalString extends OptionalValue<String>, ResourceString {

    @Override
    MandatoryString or(String defaultValue) throws IllegalArgumentException;

    @Override
    String orDefault(String defaultValue) throws IllegalArgumentException;

    @Override
    MandatoryString notNull() throws MissingValueException;

    /**
     * Shortcut to <code>ofType(type).notNull().asIs()</code>
     * @param type
     * @param <T>
     * @return
     * @throws MissingValueException
     * @since 3.1
     */
    default <T> T required(Class<T> type) throws MissingValueException {
        return ofType(type).notNull().asIs();
    }

}
