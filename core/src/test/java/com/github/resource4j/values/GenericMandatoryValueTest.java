package com.github.resource4j.values;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MandatoryValueContracts;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.values.GenericMandatoryValue;

public class GenericMandatoryValueTest extends MandatoryValueContracts {

	@Override
	protected MandatoryValue<String> createMandatoryValue(
			String resolvedSource, ResourceKey key, String object) {
		return new GenericMandatoryValue<String>(resolvedSource, key, object);
	}

}
