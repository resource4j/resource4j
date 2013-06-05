package com.github.resource4j;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.files.ResourceParsers.string;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import com.github.resource4j.files.ResourceFile;

import example.impl.ConcreteAction;

public class DefaultResourcesTest {

    @Test
    public void testGetValueFromExistingBundle() {
        DefaultResources resources = new DefaultResources();
        OptionalString value = resources.get(key("test", "value"), Locale.US);
        assertEquals("success", value.asIs());
    }

    @Test
    public void testGetValueFromDefaultBundleWhenSearchingInExistingBundle() {
        DefaultResources resources = new DefaultResources();
        OptionalString value = resources.get(key("test", "defaultValue"), Locale.US);
        assertEquals("success", value.asIs());
    }

    @Test
    public void testGetValueFromExistingBundleForType() {
        DefaultResources resources = new DefaultResources();
        OptionalString value = resources.get(key(ConcreteAction.class, "name"), Locale.US);
        assertEquals("Concrete Action", value.asIs());
    }

    @Test
    public void testGetValueFromDefaultBundleWhenSearchingInExistingBundleForType() {
        DefaultResources resources = new DefaultResources();
        OptionalString value = resources.get(key(ConcreteAction.class, "defaultName"), Locale.US);
        assertEquals("ActionName", value.asIs());
    }

    @Test
    public void testGetValueFromDefaultBundleFullPathWhenSearchingInExistingBundleForType() {
        DefaultResources resources = new DefaultResources();
        OptionalString value = resources.get(key(ConcreteAction.class, "nameByPath"), Locale.US);
        assertEquals("ActionNameByPath", value.asIs());
    }

    @Test
    public void testGetBinaryDataFromExistingBundle() {
        DefaultResources resources = new DefaultResources();
        ResourceFile file = resources.contentOf(key("test", "defaultValue"), Locale.US);
        assertEquals("success", file.parsedTo(string()).asIs());
    }

}
