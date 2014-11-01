package com.github.resource4j.generic;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.ResourceKey;

/**
 * Generic implementation of {@link MandatoryValue}.
 * @param <V> type of managed value
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericMandatoryValue<V> extends GenericResourceValue<V> implements MandatoryValue<V> {

    protected GenericMandatoryValue(String resolvedSource, ResourceKey key, V value) {
        this(resolvedSource, key, value, null);
    }

    protected GenericMandatoryValue(String resolvedSource, 
    		ResourceKey key, V value, Throwable suppressedException) {
        super(resolvedSource, key, value);
        if (value == null) throw new MissingValueException(key, suppressedException);
    }

	@Override
	public String resolvedSource() {
		return resolvedSource;
	}

}
