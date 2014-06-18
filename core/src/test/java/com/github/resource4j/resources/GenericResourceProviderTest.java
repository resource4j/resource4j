package com.github.resource4j.resources;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class GenericResourceProviderTest {

	private static final ResourceKey PROVIDER_SCOPE = ResourceKey.key("example", "provider");

	private InMemoryResources resources;
	
	private ResourceProvider provider;
	
	@Before
	public void createProvider() {
		resources = new InMemoryResources();
		provider = resources.forKey(PROVIDER_SCOPE);
	}
	
	@Test
	public void testGetValue() throws Exception {
		String someKey = "test";
		String someValue = "test value";
		givenExists(PROVIDER_SCOPE.child(someKey), someValue, in(Locale.US));
		assertEquals(someValue, provider.get(someKey, in(Locale.US)).asIs());
	}

	private void givenExists(ResourceKey key, String value, ResourceResolutionContext context) {
		resources.put(key, context, value);
	}
	
}
