package com.github.resource4j.resources.discovery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.jupiter.api.Assertions.*;

public class BasicResourceFileEnumerationStrategyTest {

    private BasicResourceFileEnumerationStrategy strategy;

    @BeforeEach
    public void setup() {
        strategy = new BasicResourceFileEnumerationStrategy();
    }

    @AfterEach
    public void shutdown() {
        strategy = null;
    }
    
    @Test
    public void testEnumerateFileNameOptionsWithComplexContext() {
        List<String> options = strategy.enumerateFileNameOptions(
        		new String[] { "config.xml" }, in("localhost", "application", "debug"));
        assertEquals(8, options.size());
        assertEquals("config-localhost-application-debug.xml", options.get(0));
        assertEquals("config-application-debug.xml", options.get(1));
        assertEquals("config-localhost-debug.xml", options.get(2));
        assertEquals("config-debug.xml", options.get(3));
        assertEquals("config-localhost-application.xml", options.get(4));
        assertEquals("config-application.xml", options.get(5));
        assertEquals("config-localhost.xml", options.get(6));
        assertEquals("config.xml", options.get(7));

    }

    @Test
    public void testEnumerateFileNameOptionsWithContext() {
        List<String> options = strategy.enumerateFileNameOptions(new String[] { "test.html" }, in(Locale.US, "WEB"));
        assertEquals(6, options.size());
        assertEquals("test-en_US-WEB.html", options.get(0));
        assertEquals("test-en-WEB.html", options.get(1));
        assertEquals("test-WEB.html", options.get(2));
        assertEquals("test-en_US.html", options.get(3));
        assertEquals("test-en.html", options.get(4));
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
        assertEquals("test-en_US.", options.get(0));
        assertEquals("test-en.", options.get(1));
        assertEquals("test.", options.get(2));
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
        assertEquals("resource-en.properties", options.get(1));
        assertEquals("resource.properties", options.get(2));
        assertEquals("default-en_US.properties", options.get(3));
        assertEquals("default-en.properties", options.get(4));
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
        assertEquals("resource-en-WEB.properties", options.get(1));
        assertEquals("resource-WEB.properties", options.get(2));
        assertEquals("resource-en_US.properties", options.get(3));
        assertEquals("resource-en.properties", options.get(4));
        assertEquals("resource.properties", options.get(5));
        assertEquals("default-en_US-WEB.properties", options.get(6));
        assertEquals("default-en-WEB.properties", options.get(7));
        assertEquals("default-WEB.properties", options.get(8));
        assertEquals("default-en_US.properties", options.get(9));
        assertEquals("default-en.properties", options.get(10));
        assertEquals("default.properties", options.get(11));
    }

}
