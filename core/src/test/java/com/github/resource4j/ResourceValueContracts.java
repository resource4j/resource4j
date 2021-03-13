package com.github.resource4j;

import org.junit.jupiter.api.Test;

import static com.github.resource4j.ResourceKey.key;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class ResourceValueContracts {

	protected abstract ResourceValue<String> createValue(ResourceKey key, String content);

	@Test
	public void testResourceValueAsIsReturnsSameObject() throws Exception {
		String content = someContent();
		ResourceValue<String> value = createValue(anyKey(), content);
		assertSame(content, value.asIs());
	}
	
	@Test
	public void testResourceValueKeyReturnsSameKey() throws Exception {
		String content = someContent();
		ResourceKey key = key("specific", "key");
		ResourceValue<String> value = createValue(key, content);
		assertSame(key, value.key());
	}

	protected ResourceKey anyKey() {
		return key("bundle", "id");
	}
	
	protected String someContent() {
		return "Some content";
	}
	
}
