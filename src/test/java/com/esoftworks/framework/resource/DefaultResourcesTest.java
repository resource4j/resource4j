package com.esoftworks.framework.resource;

import static com.esoftworks.framework.resource.ResourceKey.key;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import example.impl.ConcreteAction;

public class DefaultResourcesTest {

	@Test
	public void testGetValueFromExistingBundle() {
		DefaultResources resources = new DefaultResources();
		OptionalString value = resources.get(key("test", "value"));
		assertEquals("success", value.asIs());
	}
	
	@Test
	public void testGetValueFromDefaultBundleWhenSearchingInExistingBundle() {
		DefaultResources resources = new DefaultResources();
		OptionalString value = resources.get(key("test", "defaultValue"));
		assertEquals("success", value.asIs());
	}
	
	@Test
	public void testGetValueFromExistingBundleForType() {
		DefaultResources resources = new DefaultResources();
		OptionalString value = resources.get(key(ConcreteAction.class, "name"));
		assertEquals("Concrete Action", value.asIs());
	}
	
	@Test
	public void testGetValueFromDefaultBundleWhenSearchingInExistingBundleForType() {
		DefaultResources resources = new DefaultResources();
		OptionalString value = resources.get(key(ConcreteAction.class, "defaultName"));
		assertEquals("ActionName", value.asIs());
	}

	@Test
	public void testGetValueFromDefaultBundleFullPathWhenSearchingInExistingBundleForType() {
		DefaultResources resources = new DefaultResources();
		OptionalString value = resources.get(key(ConcreteAction.class, "nameByPath"));
		assertEquals("ActionNameByPath", value.asIs());
	}

}
