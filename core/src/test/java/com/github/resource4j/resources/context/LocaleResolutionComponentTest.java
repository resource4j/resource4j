package com.github.resource4j.resources.context;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class LocaleResolutionComponentTest {

	@Test
	public void testReduce2to1() throws Exception {
		LocaleResolutionComponent component = new LocaleResolutionComponent(Locale.US);
		LocaleResolutionComponent reduced = component.reduce();
		assertEquals(Locale.ENGLISH, reduced.locale());
	}
	
	@Test
	public void test1NotReducible() throws Exception {
		LocaleResolutionComponent component = new LocaleResolutionComponent(Locale.ENGLISH);
		assertFalse(component.isReducible());
	}
	
}
