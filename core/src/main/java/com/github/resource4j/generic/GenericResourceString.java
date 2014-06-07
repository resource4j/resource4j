package com.github.resource4j.generic;

import java.text.Format;
import java.text.MessageFormat;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceString;
import com.github.resource4j.util.TypeConverter;

public abstract class GenericResourceString extends GenericResourceValue<String> implements ResourceString {

    protected GenericResourceString(String resolvedSource, ResourceKey key, String value) {
        super(resolvedSource, key, value);
    }

    @Override
    public <T> T as(Class<T> type) {
        return TypeConverter.convert(value, type);
    }

    @Override
    public <T> T as(Class<T> type, String format) {
        return TypeConverter.convert(value, type, format);
    }

    @Override
    public <T> T as(Class<T> type, Format format) {
        return TypeConverter.convert(value, type, format);
    }

    @Override
    public String asFormatted(Object... arguments) {
        return MessageFormat.format(value, arguments);
    }

}
