package com.esoftworks.framework.resource.generic;

import java.text.Format;

import com.esoftworks.framework.resource.ResourceKey;
import com.esoftworks.framework.resource.ResourceString;
import com.esoftworks.framework.resource.util.TypeConverter;

public abstract class GenericResourceString extends GenericResourceValue<String> implements ResourceString {

	protected GenericResourceString(ResourceKey key, String value) {
		super(key, value);
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

}
