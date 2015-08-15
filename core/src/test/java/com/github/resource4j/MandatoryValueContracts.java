package com.github.resource4j;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class MandatoryValueContracts extends ResourceValueContracts {

	@Override
	protected MandatoryValue<String> createValue(ResourceKey key, String object) {
		return createMandatoryValue(anyResolvedSource(), key, object); 
	}

	protected String anyResolvedSource() {
		return "anyResolvedSource";
	}
	
	protected abstract MandatoryValue<String> createMandatoryValue(String resolvedSource, 
			ResourceKey key, String object);
	
	@Test
	public void testResolvedSourceReturnedNotEmptyAndUnmodified() {
		String resolvedSource = "SpecificSource";
		MandatoryValue<String> value = createMandatoryValue(resolvedSource, anyKey(), someContent());
		assertSame(resolvedSource, value.resolvedSource());
	}

}
