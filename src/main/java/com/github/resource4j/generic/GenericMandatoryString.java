package com.github.resource4j.generic;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MissingValueException;
import com.github.resource4j.ResourceKey;

public class GenericMandatoryString extends GenericResourceString implements MandatoryString {

	protected GenericMandatoryString(ResourceKey key, String value) {
		super(key, value);
		if (value == null) {
			throw new MissingValueException(key);
		}
	}

	@Override
	public <T> MandatoryValue<T> ofType(Class<T> type) {
		return new GenericMandatoryValue<T>(key, as(type));
	}

}
