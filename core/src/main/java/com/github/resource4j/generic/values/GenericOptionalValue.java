package com.github.resource4j.generic.values;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ValueNotAcceptableException;

/**
 * Generic implementation of {@link OptionalValue}.
 * @param <V> type of managed value
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericOptionalValue<V> extends GenericResourceValue<V> implements OptionalValue<V> {

    private Throwable suppressedException;

    public GenericOptionalValue(String resolvedSource, ResourceKey key, V value) {
        super(resolvedSource, key, value);
    }
    
    public GenericOptionalValue(String resolvedSource, ResourceKey key, V value, Throwable suppressedException) {
        super(resolvedSource, key, value);
        this.suppressedException = suppressedException;
    }
    
    public GenericOptionalValue(String resolvedSource, ResourceKey key, Throwable suppressedException) {
        super(resolvedSource, key, null);
        this.suppressedException = suppressedException;
    }

    @Override
    public MandatoryValue<V> or(V defaultValue) {
        if (defaultValue == null) throw new NullPointerException("defaultValue");
        return new GenericMandatoryValue<>(resolvedSource, key, value == null ? defaultValue : value);
    }

	@Override
	public MandatoryValue<V> orElseGet(Supplier<? extends V> supplier) {
        if (supplier == null) throw new NullPointerException("supplier");
        return new GenericMandatoryValue<>(resolvedSource, key, value == null ? supplier.get() : value);
	}

    @Override
    public MandatoryValue<V> notNull() throws MissingValueException {
        return new GenericMandatoryValue<>(resolvedSource, key, value, suppressedException);
    }

	@Override
	public OptionalValue<V> filter(Predicate<V> predicate) {
		return predicate.test(value) ? 
				this : new GenericOptionalValue<>(resolvedSource, key, new ValueNotAcceptableException(key.toString()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <U> OptionalValue<U> map(Function<? super V, ? extends U> mapper) {
		return value == null ? (OptionalValue) this : 
			new GenericOptionalValue<U>(resolvedSource, key, mapper.apply(value));
	}

	@Override
	public Throwable suppressedException() {
		return suppressedException;
	}

}
