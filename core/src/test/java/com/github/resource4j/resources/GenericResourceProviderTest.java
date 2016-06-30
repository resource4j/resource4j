package com.github.resource4j.resources;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.Assert.*;

import java.util.Locale;

import com.github.resource4j.AbstractResource4JTest;
import org.junit.Before;
import org.junit.Test;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public class GenericResourceProviderTest extends AbstractResource4JTest {

	private static final ResourceKey PROVIDER_SCOPE = ResourceKey.key("example", "provider");

	@Test
	public void testGetValue() throws Exception {
		String id = "test";
		String someValue = "test value";
		ResourceKey valueKey = PROVIDER_SCOPE.child(id);

		givenExists(valueKey, in(Locale.US), someValue);

		ResourceProvider provider = resources().forKey(PROVIDER_SCOPE);

		assertEquals(someValue, provider.get(id, in(Locale.US)).asIs());
	}

}
