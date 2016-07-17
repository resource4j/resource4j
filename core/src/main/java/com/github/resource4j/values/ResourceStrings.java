package com.github.resource4j.values;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;

/**
 * Functional namespace for OptionalString factory methods
 */
public final class ResourceStrings {

    private static final String RUNTIME_SOURCE = "runtime:";

    private static final ResourceKey DEFAULT_KEY = ResourceKey.key(ResourceStrings.class, "value");

    private ResourceStrings() {
    }

    /**
     * Returns new instance of OptionalString with null string
     * @return empty OptionalString
     */
    public static OptionalString asNull() {
        return new GenericOptionalString(RUNTIME_SOURCE, DEFAULT_KEY, null);
    }

    /**
     * Returns new instance of OptionalString with given value
     * @param value wrapped object
     * @return given object wrapped in OptionalString
     */
    public static OptionalString asString(String value) {
        return new GenericOptionalString(RUNTIME_SOURCE, DEFAULT_KEY, value);
    }

    /**
     * Returns new instance of OptionalString with given key and value
     * @param key key of the returned OptionalString
     * @param value wrapped string
     * @return given object wrapped in OptionalString with given key
     */
    public static OptionalString asString(ResourceKey key, String value) {
        return new GenericOptionalString(RUNTIME_SOURCE, key, value);
    }

}
