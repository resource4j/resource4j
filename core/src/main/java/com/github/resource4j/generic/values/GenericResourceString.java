package com.github.resource4j.generic.values;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceString;

/**
 * Generic implementation of {@link ResourceString}.
 * @author Ivan Gammel
 * @since 1.0
 */
public abstract class GenericResourceString extends GenericResourceValue<String> implements ResourceString {

    protected GenericResourceString(String resolvedSource, ResourceKey key, String value) {
        super(resolvedSource, key, value);
    }

}
