package com.github.resource4j.generic.values;

import java.text.Format;
import java.text.MessageFormat;
import java.util.function.Function;

import com.github.resource4j.*;
import com.github.resource4j.util.TypeCastException;
import com.github.resource4j.util.TypeConverter;

/**
 * Generic implementation of {@link MandatoryString}.
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericMandatoryString extends GenericResourceString implements MandatoryString {

    public GenericMandatoryString(String resolvedSource, ResourceKey key, String value) {
        this(resolvedSource, key, value, null);
    }

    protected GenericMandatoryString(String resolvedSource, ResourceKey key, String value, Throwable cause) {
        super(resolvedSource, key, value);
        if (value == null) {
            throw new MissingValueException(key, cause);
        }
    }

    @Override
    public <T> T as(Class<T> type) {
        return ofType(type).asIs();
    }

    @Override
    public <T> T as(Class<T> type, String format) {
        return ofType(type, format).asIs();
    }

    @Override
    public <T> T as(Class<T> type, Format format) {
        return ofType(type, format).asIs();
    }

    @Override
    public String asFormatted(Object... arguments) {
        return MessageFormat.format(value, arguments);
    }

    @Override
    public <U> OptionalValue<U> map(Function<? super String, ? extends U> mapper) {
        return new GenericOptionalValue<U>(resolvedSource, key, mapper.apply(value));
    }

    @Override
    public <T> MandatoryValue<T> ofType(Class<T> type) throws TypeCastException {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type));
    }

    @Override
    public MandatoryString asString() throws TypeCastException {
        return this;
    }

    @Override
    public MandatoryString asString(String format) throws TypeCastException {
        return new GenericMandatoryString(resolvedSource, key, String.format(format, value));
    }

    @Override
    public MandatoryString asString(Format format) throws TypeCastException {
        return new GenericMandatoryString(resolvedSource, key, format.format(value));
    }

    @Override
	public String resolvedSource() {
		return resolvedSource;
	}

	@Override
	public <T> MandatoryValue<T> ofType(Class<T> type, String format) throws TypeCastException {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type, format));
	}

	@Override
	public <T> MandatoryValue<T> ofType(Class<T> type, Format format) throws TypeCastException {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type, format));
	}

}
