package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.key;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public abstract class AbstractResourceBundleParserTest {

    private ResourceBundleParser parser;

    public ResourceBundleParser getParser() {
        return parser;
    }

    public void setParser(ResourceBundleParser parser) {
        this.parser = parser;
    }

    @Test
    public void testNullFileNameForNullBundle() {
        String fileName = parser.getResourceFileName(key("test"));
        assertNull(fileName);
    }

}
