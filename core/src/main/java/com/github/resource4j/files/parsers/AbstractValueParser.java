package com.github.resource4j.files.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.generic.GenericOptionalValue;

public abstract class AbstractValueParser<T> extends AbstractParser<T, OptionalValue<T>> {

    @Override
    protected OptionalValue<T> createValue(ResourceFile file, ResourceKey key, T value, Throwable suppressedException) {
        if (suppressedException != null) {
            return new GenericOptionalValue<>(file.resolvedName(), key, suppressedException);
        } else {
            return new GenericOptionalValue<>(file.resolvedName(), key, value);
        }
    }

}
