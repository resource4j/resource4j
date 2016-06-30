package com.github.resource4j.resources.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringResolutionComponentTest {

	@Test
	public void testReductionFrom10To1() throws Exception {
		StringResolutionComponent component = new StringResolutionComponent("1","2","3","4","5","6","7","8","9","10");
		for (int i = 10; i >= 2; i--) {
			assertEquals(String.valueOf(i), component.sections().get(i-1));
			component = component.reduce();
		}
		assertFalse(component.isReducible());
		assertEquals(1, component.sections().size());
		assertEquals("1", component.sections().get(0));
	}
	
}
