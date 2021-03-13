package com.github.resource4j.resources.context;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultResolutionContextMatcherTest {
	
	private ResolutionContextMatcher matcher = new DefaultResolutionContextMatcher();
	
	@Test
	public void testSingleStringMatches() {
		ResourceResolutionContext single = in("A");
		List<ResourceResolutionContext> matches = matcher.matches(single);
		assertEquals(2, matches.size());
		assertEquals("A", matches.get(0).toString());
		assertEquals(0, matches.get(1).components().length);
	}

	@Test
	public void test2SectionsMatches() {
		ResourceResolutionContext single = in((Object) new String[] {"A","B"});
		List<ResourceResolutionContext> matches = matcher.matches(single);
		assertEquals(3, matches.size());
		assertEquals("A_B", matches.get(0).toString());
		assertEquals("A", matches.get(1).toString());
		assertEquals(0, matches.get(2).components().length);
	}
	
	@Test
	public void test2CompositeMatches() {
		ResourceResolutionContext single = in(Locale.US, "web");
		List<ResourceResolutionContext> matches = matcher.matches(single);
		System.out.println(matches.stream()
				.map(ResourceResolutionContext::toString)
				.collect(Collectors.joining("\n")));
		assertEquals(6, matches.size());
		assertEquals("en_US-web", matches.get(0).toString());
		assertEquals("en-web", matches.get(1).toString());
		assertEquals("en_US", matches.get(2).toString());
		assertEquals("en", matches.get(3).toString());
		assertEquals("web", matches.get(4).toString());
		assertEquals(0, matches.get(5).components().length);
	}
	
	@Test
	public void test3StringMatches() {
		ResourceResolutionContext single = in("app", "env", "ctx");
		List<ResourceResolutionContext> matches = matcher.matches(single);
		assertEquals(7, matches.size());
		assertEquals("app-env-ctx", matches.get(0).toString());
		assertEquals("app-ctx", matches.get(1).toString());
		assertEquals("app", matches.get(2).toString());
		assertEquals("env-ctx", matches.get(3).toString());
		assertEquals("env", matches.get(4).toString());
		assertEquals("ctx", matches.get(5).toString());
		assertEquals(0, matches.get(6).components().length);
	}	

}
