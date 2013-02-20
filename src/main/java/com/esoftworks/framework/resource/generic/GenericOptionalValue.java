package com.esoftworks.framework.resource.generic;

import com.esoftworks.framework.resource.MandatoryValue;
import com.esoftworks.framework.resource.MissingValueException;
import com.esoftworks.framework.resource.OptionalValue;
import com.esoftworks.framework.resource.ResourceKey;

public class GenericOptionalValue<V> extends GenericResourceValue<V> implements OptionalValue<V> {

	public GenericOptionalValue(ResourceKey key, V value) {
		super(key, value);
	}

	@Override
	public V orDefault(V defaultValue) {
		if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
		if (value == null) return defaultValue;
		return value;
	}

	@Override
	public MandatoryValue<V> or(V defaultValue) {
		if (defaultValue == null) throw new IllegalArgumentException("defaultValue");
		return new GenericMandatoryValue<V>(key, value == null ? defaultValue : value);
	}

	@Override
	public MandatoryValue<V> notNull() throws MissingValueException {
		return new GenericMandatoryValue<V>(key, value);
	}
}
