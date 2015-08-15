package com.github.resource4j.generic;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.MandatoryStringContracts;
import com.github.resource4j.ResourceKey;

public class GenericMandatoryStringTest extends MandatoryStringContracts {

	@Override
	protected MandatoryString createMandatoryValue(String resolvedSource,
			ResourceKey key, String object) {
		return new GenericMandatoryString(resolvedSource, key, object);	
	}
	
}
