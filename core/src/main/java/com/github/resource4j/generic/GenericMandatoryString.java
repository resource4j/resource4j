package com.github.resource4j.generic;

import java.text.Format;
import java.text.MessageFormat;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceValue;
import com.github.resource4j.util.TypeConverter;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class GenericMandatoryString extends GenericResourceString implements MandatoryString {

    protected GenericMandatoryString(String resolvedSource, ResourceKey key, String value) {
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
    public <T> MandatoryValue<T> ofType(Class<T> type) {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type));
    }

	@Override
	public String resolvedSource() {
		return resolvedSource;
	}

	@Override
	public <T> ResourceValue<T> ofType(Class<T> type, String format) {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type, format));
	}

	@Override
	public <T> ResourceValue<T> ofType(Class<T> type, Format format) {
        return new GenericMandatoryValue<>(resolvedSource, key, TypeConverter.convert(value, type, format));
	}

}
