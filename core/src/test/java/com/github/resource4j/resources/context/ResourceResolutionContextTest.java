package com.github.resource4j.resources.context;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static org.junit.Assert.*;

public class ResourceResolutionContextTest {

	@Test
	public void testLocaleParentsEnumeratedCorrectly() {
        ResourceResolutionContext context = in(Locale.GERMANY);
        List<ResourceResolutionContext> contexts = new ArrayList<>();
        do {
            context = context.parent();
            contexts.add(context);
        } while (!context.isEmpty());

        assertEquals(2, contexts.size());
        assertEquals(in(Locale.GERMAN), contexts.get(0));
        assertEquals(withoutContext(), contexts.get(1));

    }

	@Test
	public void testFactoryMethodResultToStringIsCorrect() {
		ResourceResolutionContext context = in("A", Locale.US, "B");
		assertEquals("A-en_US-B", context.toString());
	}
	
	@Test
	public void testGenericContainsLocale() {
		ResourceResolutionContext generic = in();
		ResourceResolutionContext locale = in(Locale.US);
		assertTrue(generic.contains(locale));
	}
	
	@Test
	public void testLanguageContainsCountry() {
		ResourceResolutionContext language = in(Locale.ENGLISH);
		ResourceResolutionContext country = in(Locale.US);
		assertTrue(language.contains(country));
	}

	@Test
	public void testShorterContainsLonger() {
		ResourceResolutionContext parent = in("1", "2");
		ResourceResolutionContext child = in("1", "2", "3", "4");
		assertTrue(parent.contains(child));
	}	
	
	@Test
	public void testComplexContains() {
		ResourceResolutionContext parent = in("A", Locale.ENGLISH, "Test");
		ResourceResolutionContext child = in("A", Locale.US, "Test", "B");
		assertTrue(parent.contains(child));
	}

	@Test
	public void testContextsWithoutParametersEqual() {
		ResourceResolutionContext first = in("A", Locale.US, "Test");
		ResourceResolutionContext second = in("A", Locale.US, "Test");
		assertEquals(first, second);
	}

	@Test
	public void testContextsWithSameParametersEqual() {
		ResourceResolutionContext first = in("A", Locale.US, "Test").add("count", 1);
		ResourceResolutionContext second = in("A", Locale.US, "Test").add("count", 1);
		assertEquals(first, second);
	}

	@Test
	public void testContextsWithDifferentParameterValuesNotEqual() {
		ResourceResolutionContext first = in("A", Locale.US, "Test").add("count", 1);
		ResourceResolutionContext second = in("A", Locale.US, "Test").add("count", 2);
		assertNotEquals(first, second);
	}

	@Test
	public void testContextWithoutParametersNotEqualToContextWithParmeters() {
		ResourceResolutionContext first = in("A", Locale.US, "Test");
		ResourceResolutionContext second = in("A", Locale.US, "Test").add("count", 1);
		assertNotEquals(first, second);
	}

	@Test
	public void testContextWithParametersNotEqualToContextWithoutParmeters() {
		ResourceResolutionContext first = in("A", Locale.US, "Test").add("count", 1);
		ResourceResolutionContext second = in("A", Locale.US, "Test");
		assertNotEquals(first, second);
	}
}
