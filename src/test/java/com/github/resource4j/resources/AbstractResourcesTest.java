package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.files.parsers.ResourceParsers.string;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.resources.Resources;

import example.impl.ConcreteAction;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractResourcesTest {

    protected Resources resources;

    @Before
    public void setup() {
        resources = createResources();
    }

    protected abstract Resources createResources();

    public void shutdown() {
        resources = null;
    }

    @Test
    public void testGetValueFromExistingBundle() {
        MandatoryString value = resources.get(key("test", "value"), Locale.US).notNull();
        assertEquals("success", value.asIs());
    }

    @Test
    public void testGetValueFromDefaultBundleWhenSearchingInExistingBundle() {
        MandatoryString value = resources.get(key("test", "defaultValue"), Locale.US).notNull();
        assertEquals("success", value.asIs());
    }

    @Test
    public void testGetValueFromExistingBundleSpecifiedByClass() {
        MandatoryString value = resources.get(key(ConcreteAction.class, "name"), Locale.US).notNull();
        assertEquals("Concrete Action", value.asIs());
    }

    @Test
    public void testGetValueFromExistingBundleSpecifiedByClassAndExistingCountryLocale() {
        MandatoryString value = resources.get(key(ConcreteAction.class, "name"), Locale.GERMANY).notNull();
        assertEquals("Aktion", value.asIs());
    }

    @Test
    public void testGetValueFromExistingBundleSpecifiedByClassAndExistingLanguageLocale() {
        MandatoryString value = resources.get(key(ConcreteAction.class, "name"), Locale.GERMAN).notNull();
        assertEquals("Konkret Aktion", value.asIs());
    }

    @Test
    public void testGetValueFromDefaultBundleWhenSearchingInExistingBundleSpecifiedByClass() {
        MandatoryString value = resources.get(key(ConcreteAction.class, "defaultName"), Locale.US).notNull();
        assertEquals("ActionName", value.asIs());
    }

    @Test
    public void testGetBinaryDataFromExistingBundle() {
        assertEquals("value=success",
                resources.contentOf("test.properties", Locale.US).parsedTo(string()).asIs().trim());
    }

    @Test
    public void testGetBinaryDataFromExistingBundleForSpecificLocale() {
        assertEquals("value=Erfolg",
                resources.contentOf("test.properties", Locale.GERMANY).parsedTo(string()).asIs().trim());
    }

}
