package com.github.resource4j.values;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

/**
 * Functional namespace for OptionalValue factory methods
 */
public final class ResourceValues {

    private static final String RUNTIME_SOURCE = "runtime:";

    private static final ResourceKey DEFAULT_KEY = ResourceKey.key(ResourceValues.class, "value");

    private ResourceValues() {
    }

    /**
     * Returns new instance of OptionalValue with null value
     * @param <T> type of the wrapped value
     * @return empty OptionalValue
     */
    public static <T> OptionalValue<T> empty() {
        return new GenericOptionalValue<>(RUNTIME_SOURCE, DEFAULT_KEY, (T) null);
    }

    /**
     * Returns new instance of OptionalValue with given value
     * @param value wrapped object
     * @param <T> type of the wrapped object
     * @return given object wrapped in OptionalValue
     */
    public static <T> OptionalValue<T> ofNullable(T value) {
        return new GenericOptionalValue<T>(RUNTIME_SOURCE, DEFAULT_KEY, value);
    }

    /**
     * Returns new instance of OptionalValue with given key and value
     * @param key resource key of the created value
     * @param value wrapped object
     * @param <T> type of the wrapped object
     * @return given object wrapped in OptionalValue with given key
     */
    public static <T> OptionalValue<T> ofNullable(ResourceKey key, T value) {
        return new GenericOptionalValue<T>(RUNTIME_SOURCE, key, value);
    }
}
