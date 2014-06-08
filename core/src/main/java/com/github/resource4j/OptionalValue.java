package com.github.resource4j;

/**
 * @author Ivan Gammel
 * @since 1.0
 * @param <V>
 */
public interface OptionalValue<V> extends ResourceValue<V> {

    V orDefault(V defaultValue);

    MandatoryValue<V> or(V defaultValue);

    MandatoryValue<V> notNull() throws MissingValueException;

}
