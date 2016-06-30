package com.github.resource4j.values;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MandatoryStringContracts;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.values.GenericMandatoryString;

public class GenericMandatoryStringTest extends MandatoryStringContracts {

	@Override
	protected MandatoryString createMandatoryValue(String resolvedSource,
			ResourceKey key, String object) {
		return new GenericMandatoryString(resolvedSource, key, object);
	}
	
}
