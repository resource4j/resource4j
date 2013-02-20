package com.esoftworks.framework.resource.generic;

import com.esoftworks.framework.resource.MandatoryString;
import com.esoftworks.framework.resource.MandatoryValue;
import com.esoftworks.framework.resource.MissingValueException;
import com.esoftworks.framework.resource.ResourceKey;

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
