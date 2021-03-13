package com.github.resource4j.resources.discovery;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.test.Builders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.resource4j.test.Builders.given;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractResourceBundleParserTest {

    private ResourceBundleParser parser;

    @BeforeEach
    public void initParser() {
        this.parser = aParser();
    }

    @Test
    public void testSimpleKeyValuePairParsed() {
        String key = Builders.anyIdentifier();
        String value = Builders.anyIdentifier();
        ResourceObject bundle = given(aBundle().with(key,value));
        Map<String, String> values = parser.parse(bundle);
        assertEquals(1, values.size());
        assertEquals(value, values.get(key));
    }

    @Test
    public void testMultipleEntriesParsed() {
        String key1 = Builders.anyIdentifier();
        String value1 = Builders.anyIdentifier();
        String key2 = Builders.anyIdentifier();
        String value2 = Builders.anyIdentifier();
        ResourceObject bundle = given(aBundle().with(key1,value1).with(key2, value2));
        Map<String, String> values = parser.parse(bundle);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(key1));
        assertEquals(value2, values.get(key2));
    }

    @Test
    public void testUnicodeValueParsed() {
        String key = Builders.anyIdentifier();
        String value = "Honey in Russian is \"Мёд\" and in German is Hönig";
        ResourceObject bundle = given(aBundle().with(key,value));
        Map<String, String> values = parser.parse(bundle);
        assertEquals(1, values.size());
        assertEquals(value, values.get(key));
    }

    protected abstract ResourceBundleParser aParser();

    protected abstract BundleBuilder aBundle();

}
