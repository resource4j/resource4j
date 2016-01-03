package com.github.resource4j.objects.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.values.GenericOptionalValue;

public abstract class AbstractValueParser<T> extends AbstractParser<T, OptionalValue<T>> {

    @Override
    protected OptionalValue<T> createValue(ResourceObject object, ResourceKey key, T value, Throwable suppressedException) {
        if (suppressedException != null) {
            return new GenericOptionalValue<>(object.resolvedName(), key, suppressedException);
        } else {
            return new GenericOptionalValue<>(object.resolvedName(), key, value);
        }
    }

}
