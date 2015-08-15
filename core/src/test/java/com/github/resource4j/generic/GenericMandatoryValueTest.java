package com.github.resource4j.generic;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.MandatoryValueContracts;
import com.github.resource4j.ResourceKey;

public class GenericMandatoryValueTest extends MandatoryValueContracts {

	@Override
	protected MandatoryValue<String> createMandatoryValue(
			String resolvedSource, ResourceKey key, String object) {
		return new GenericMandatoryValue<String>(resolvedSource, key, object);
	}

}
