package com.esoftworks.framework.resource.generic;

import com.esoftworks.framework.resource.MandatoryValue;
import com.esoftworks.framework.resource.MissingValueException;
import com.esoftworks.framework.resource.ResourceKey;

public class GenericMandatoryValue<V> extends GenericResourceValue<V> implements MandatoryValue<V> {

	protected GenericMandatoryValue(ResourceKey key, V value) {
		super(key, value);
		if (value == null) throw new MissingValueException(key); 
	}

}
