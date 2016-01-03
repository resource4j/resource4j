package com.github.resource4j.resources;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import com.github.resource4j.ResourceKey;


public class InMemoryResourcesTest {
	
	public static final ResourceKey SOME_KEY = ResourceKey.key("inmemory", "example");
	public static final String SOME_VALUE = "some value"; 
	
	@Test
	public void testSimpleGetAndPutWithExactMatch() {
		InMemoryResources resources = new InMemoryResources();
		resources.put(SOME_KEY, Locale.US, SOME_VALUE);
		assertEquals(SOME_VALUE, resources.get(SOME_KEY, Locale.US).asIs());
	}
	
	@Test
	public void testGetAndPutInContextWithExactMatch() {
		InMemoryResources resources = new InMemoryResources();
		resources.put(SOME_KEY, in(Locale.US, "Web"), SOME_VALUE);
		assertEquals(SOME_VALUE, resources.get(SOME_KEY, in(Locale.US, "Web")).asIs());
	}	
	
	@Test
	public void testGetAndPutInContextWithPartialMatch() {
		InMemoryResources resources = new InMemoryResources();
		resources.put(SOME_KEY, in(Locale.US), SOME_VALUE);
		assertEquals(SOME_VALUE, resources.get(SOME_KEY, in(Locale.US, "Web")).asIs());
	}
	
}
