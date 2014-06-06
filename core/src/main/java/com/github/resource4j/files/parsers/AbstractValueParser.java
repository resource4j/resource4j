package com.github.resource4j.files.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.generic.GenericOptionalValue;

public abstract class AbstractValueParser<T> extends AbstractParser<T, OptionalValue<T>> {

    @Override
    protected OptionalValue<T> createValue(ResourceKey key, T value, Throwable suppressedException) {
        if (suppressedException != null) {
            return new GenericOptionalValue<>(key, suppressedException);
        } else {
            return new GenericOptionalValue<>(key, value);
        }
    }

}
