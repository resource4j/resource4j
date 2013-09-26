package com.github.resource4j.generic;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

public class GenericOptionalValue<V> extends GenericResourceValue<V> implements OptionalValue<V> {

    private Throwable suppressedException;

    public GenericOptionalValue(ResourceKey key, V value) {
        super(key, value);
    }

    public GenericOptionalValue(ResourceKey key, Throwable suppressedException) {
        super(key, null);
        this.suppressedException = suppressedException;
    }

    @Override
    public V orDefault(V defaultValue) {
        if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
        if (value == null) return defaultValue;
        return value;
    }

    @Override
    public MandatoryValue<V> or(V defaultValue) {
        if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
        return new GenericMandatoryValue<V>(key, value == null ? defaultValue : value);
    }

    @Override
    public MandatoryValue<V> notNull() throws MissingValueException {
        return new GenericMandatoryValue<V>(key, value, suppressedException);
    }
}
