package com.github.resource4j.files.lookup;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicResourceFileEnumerationStrategyTest {

    private BasicResourceFileEnumerationStrategy strategy;

    @Before
    public void setup() {
        strategy = new BasicResourceFileEnumerationStrategy();
    }

    @After
    public void shutdown() {
        strategy = null;
    }

    @Test
    public void testEnumerateUSLocaleOptions() {
        Locale locale = Locale.US;
        List<String> options = strategy.enumerateLocaleOptions(locale);
        assertEquals(3, options.size());
        assertEquals("en_US", options.get(0));
        assertEquals("en", options.get(1));
        assertEquals("", options.get(2));
    }

    @Test
    public void testEnumerateLocaleWithVariantOptions() {
        Locale locale = new Locale("ru", "RU", "cp1251");
        List<String> options = strategy.enumerateLocaleOptions(locale);
        assertEquals(4, options.size());
        assertEquals("ru_RU_cp1251", options.get(0));
        assertEquals("ru_RU", options.get(1));
        assertEquals("ru", options.get(2));
        assertEquals("", options.get(3));
    }

    @Test
    public void testEnumerateNoLocaleOptions() {
        Locale locale = null;
        List<String> options = strategy.enumerateLocaleOptions(locale);
        assertEquals(1, options.size());
        assertEquals("", options.get(0));
    }

    @Test
    public void testEnumerateFileNameOptionsWithContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test.html" }, in(Locale.US, "WEB"));
        assertEquals(6, options.size());
        assertEquals("test-en_US-WEB.html", options.get(0));
        assertEquals("test-en_US.html", options.get(1));
        assertEquals("test-en-WEB.html", options.get(2));
        assertEquals("test-en.html", options.get(3));
        assertEquals("test-WEB.html", options.get(4));
        assertEquals("test.html", options.get(5));
    }

    @Test
    public void testEnumerateFileNameOptionsWithoutContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test.html" }, in(Locale.US));
        assertEquals(3, options.size());
        assertEquals("test-en_US.html", options.get(0));
        assertEquals("test-en.html", options.get(1));
        assertEquals("test.html", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithoutExtensionAndContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test" }, in(Locale.US));
        assertEquals(3, options.size());
        assertEquals("test-en_US", options.get(0));
        assertEquals("test-en", options.get(1));
        assertEquals("test", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithDotWithoutExtensionAndContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test." }, in(Locale.US));
        assertEquals(3, options.size());
        assertEquals("test-en_US", options.get(0));
        assertEquals("test-en", options.get(1));
        assertEquals("test", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithExtensionOnlyWithoutContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { ".config" }, in(Locale.US));
        assertEquals(3, options.size());
        assertEquals("en_US.config", options.get(0));
        assertEquals("en.config", options.get(1));
        assertEquals(".config", options.get(2));
    }
    @Test
    public void testEnumerateFileNameOptionsWithDefaultBundleWithoutContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] {
                "resource.properties",
                "default.properties"
        }, in(Locale.US));
        assertEquals(6, options.size());
        assertEquals("resource-en_US.properties", options.get(0));
        assertEquals("default-en_US.properties", options.get(1));
        assertEquals("resource-en.properties", options.get(2));
        assertEquals("default-en.properties", options.get(3));
        assertEquals("resource.properties", options.get(4));
        assertEquals("default.properties", options.get(5));
    }

    @Test
    public void testEnumerateFileNameOptionsWithDefaultBundleWithContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] {
                "resource.properties",
                "default.properties"
        }, in(Locale.US, "WEB"));
        assertEquals(12, options.size());
        assertEquals("resource-en_US-WEB.properties", options.get(0));
        assertEquals("resource-en_US.properties", options.get(1));
        assertEquals("default-en_US-WEB.properties", options.get(2));
        assertEquals("default-en_US.properties", options.get(3));
        assertEquals("resource-en-WEB.properties", options.get(4));
        assertEquals("resource-en.properties", options.get(5));
        assertEquals("default-en-WEB.properties", options.get(6));
        assertEquals("default-en.properties", options.get(7));
        assertEquals("resource-WEB.properties", options.get(8));
        assertEquals("resource.properties", options.get(9));
        assertEquals("default-WEB.properties", options.get(10));
        assertEquals("default.properties", options.get(11));
    }

}
