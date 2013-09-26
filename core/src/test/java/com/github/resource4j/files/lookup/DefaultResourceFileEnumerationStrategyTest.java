package com.github.resource4j.files.lookup;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultResourceFileEnumerationStrategyTest {

    private DefaultResourceFileEnumerationStrategy strategy;

    @Before
    public void setup() {
        strategy = new DefaultResourceFileEnumerationStrategy();
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
        givenDefinedContext("WEB");
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test.html" }, Locale.US);
        assertEquals(6, options.size());
        assertEquals("test-WEB-en_US.html", options.get(0));
        assertEquals("test-en_US.html", options.get(1));
        assertEquals("test-WEB-en.html", options.get(2));
        assertEquals("test-en.html", options.get(3));
        assertEquals("test-WEB.html", options.get(4));
        assertEquals("test.html", options.get(5));
    }

    @Test
    public void testEnumerateFileNameOptionsWithoutContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test.html" }, Locale.US);
        assertEquals(3, options.size());
        assertEquals("test-en_US.html", options.get(0));
        assertEquals("test-en.html", options.get(1));
        assertEquals("test.html", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithoutExtensionAndContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test" }, Locale.US);
        assertEquals(3, options.size());
        assertEquals("test-en_US", options.get(0));
        assertEquals("test-en", options.get(1));
        assertEquals("test", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithDotWithoutExtensionAndContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test." }, Locale.US);
        assertEquals(3, options.size());
        assertEquals("test-en_US", options.get(0));
        assertEquals("test-en", options.get(1));
        assertEquals("test", options.get(2));
    }

    @Test
    public void testEnumerateFileNameOptionsWithExtensionOnlyWithoutContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { ".config" }, Locale.US);
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
        }, Locale.US);
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
        givenDefinedContext("WEB");
        List<String> options = strategy.enumerateFileNameOptions(new String[] {
                "resource.properties",
                "default.properties"
        }, Locale.US);
        assertEquals(12, options.size());
        assertEquals("resource-WEB-en_US.properties", options.get(0));
        assertEquals("resource-en_US.properties", options.get(1));
        assertEquals("default-WEB-en_US.properties", options.get(2));
        assertEquals("default-en_US.properties", options.get(3));
        assertEquals("resource-WEB-en.properties", options.get(4));
        assertEquals("resource-en.properties", options.get(5));
        assertEquals("default-WEB-en.properties", options.get(6));
        assertEquals("default-en.properties", options.get(7));
        assertEquals("resource-WEB.properties", options.get(8));
        assertEquals("resource.properties", options.get(9));
        assertEquals("default-WEB.properties", options.get(10));
        assertEquals("default.properties", options.get(11));
    }


    private void givenDefinedContext(final String context) {
        strategy.setContextProvider(new ResourceContextProvider() {
            @Override
            public String getResourceContext() {
                return context;
            }
        });
    }

}
