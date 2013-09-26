package com.github.resource4j.generic;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.ResourceKey;

public class GenericMandatoryValue<V> extends GenericResourceValue<V> implements MandatoryValue<V> {

    protected GenericMandatoryValue(ResourceKey key, V value) {
        this(key, value, null);
    }

    protected GenericMandatoryValue(ResourceKey key, V value, Throwable suppressedException) {
        super(key, value);
        if (value == null) throw new MissingValueException(key, suppressedException);
    }

}
