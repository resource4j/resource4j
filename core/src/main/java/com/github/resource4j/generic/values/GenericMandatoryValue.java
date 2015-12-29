package com.github.resource4j.generic.values;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

import java.util.function.Function;

/**
 * Generic implementation of {@link MandatoryValue}.
 * @param <V> type of managed value
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericMandatoryValue<V> extends GenericResourceValue<V> implements MandatoryValue<V> {

    public GenericMandatoryValue(String resolvedSource, ResourceKey key, V value) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U> OptionalValue<U> map(Function<? super V, ? extends U> mapper) {
        return new GenericOptionalValue<U>(resolvedSource, key, mapper.apply(value));
    }

}
