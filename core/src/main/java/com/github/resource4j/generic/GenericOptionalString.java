package com.github.resource4j.generic;

import java.text.Format;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ValueNotAcceptableException;
import com.github.resource4j.util.TypeCastException;
import com.github.resource4j.util.TypeConverter;

/**
 * Generic implementation of {@link OptionalString}.
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericOptionalString extends GenericResourceString implements OptionalString {

    private Throwable suppressedException;

    public GenericOptionalString(String resolvedSource, ResourceKey key, String value) {
        super(resolvedSource, key, value);
    }

    public GenericOptionalString(String resolvedSource, ResourceKey key, 
    		String value, Throwable suppressedException) {
        super(resolvedSource, key, value);
        this.suppressedException = suppressedException;
    }

    @Override
    public String orDefault(String defaultValue) {
        if (defaultValue == null) throw new NullPointerException("defaultValue");
        if (value == null) return defaultValue;
        return value;
    }

    @Override
    public MandatoryString or(String defaultValue) throws IllegalArgumentException {
        if (defaultValue == null) throw new NullPointerException("defaultValue");
        return new GenericMandatoryString(resolvedSource, key, value == null ? defaultValue : value);
    }

    @Override
    public MandatoryString notNull() throws MissingValueException {
        return new GenericMandatoryString(resolvedSource, key, value, suppressedException);
    }
    
    @Override
    public <T> OptionalValue<T> ofType(Class<T> type, Format format) throws TypeCastException {
        T as = null;
        if (suppressedException == null) {
            as = TypeConverter.convert(value, type, format);
        }
        if (suppressedException == null) {
            return new GenericOptionalValue<>(resolvedSource, key, as);
        } else {
            return new GenericOptionalValue<>(resolvedSource, key, suppressedException);
        }
    }
    
    @Override
    public <T> OptionalValue<T> ofType(Class<T> type, String format) throws TypeCastException {
        T as = null;
        if (suppressedException == null) {
            as = TypeConverter.convert(value, type, format);
        }
        if (suppressedException == null) {
            return new GenericOptionalValue<>(resolvedSource, key, as);
        } else {
            return new GenericOptionalValue<>(resolvedSource, key, suppressedException);
        }
    }

    @Override
    public <T> OptionalValue<T> ofType(Class<T> type) throws TypeCastException {
        T as = null;
        if (suppressedException == null) {
            as = TypeConverter.convert(value, type);
        }
        if (suppressedException == null) {
            return new GenericOptionalValue<>(resolvedSource, key, as);
        } else {
            return new GenericOptionalValue<>(resolvedSource, key, suppressedException);
        }
    }

	@Override
	public OptionalString filter(Predicate<String> predicate) {
		return predicate.test(value) ? 
				this : new GenericOptionalString(resolvedSource, key, null, new ValueNotAcceptableException(key.toString()));
	}

	@Override
	public <U> OptionalValue<U> map(Function<? super String, ? extends U> mapper) {
		return value == null ? 
			new GenericOptionalValue<U>(resolvedSource, key, suppressedException)
			: new GenericOptionalValue<U>(resolvedSource, key, mapper.apply(value));
	}

	@Override
	public MandatoryString orElseGet(Supplier<? extends String> supplier) {
        if (supplier == null) throw new NullPointerException("supplier");
        return new GenericMandatoryString(resolvedSource, key, value == null ? supplier.get() : value);
	}

}
