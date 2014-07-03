package com.github.resource4j.generic;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceString;

public abstract class GenericResourceString extends GenericResourceValue<String> implements ResourceString {

    protected GenericResourceString(String resolvedSource, ResourceKey key, String value) {
        super(resolvedSource, key, value);
    }

}
