package com.github.resource4j.generic;

import java.text.Format;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.util.TypeConverter;

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
        if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
        if (value == null) return defaultValue;
        return value;
    }

    @Override
    public MandatoryString or(String defaultValue) {
        if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
        return new GenericMandatoryString(resolvedSource, key, value == null ? defaultValue : value);
    }

    @Override
    public MandatoryString notNull() throws MissingValueException {
        return new GenericMandatoryString(resolvedSource, key, value, suppressedException);
    }
    
    @Override
    public <T> OptionalValue<T> ofType(Class<T> type, Format format) {
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
    public <T> OptionalValue<T> ofType(Class<T> type, String format) {
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
    public <T> OptionalValue<T> ofType(Class<T> type) {
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

}
