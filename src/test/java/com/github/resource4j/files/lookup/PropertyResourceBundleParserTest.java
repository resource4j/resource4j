package com.github.resource4j.files.lookup;

import static com.github.resource4j.ResourceKey.key;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PropertyResourceBundleParserTest extends AbstractResourceBundleParserTest {

    @Before
    public void createParser() {
        setParser(new PropertyResourceBundleParser());
    }

    @Test
    public void testFileNameValidForValidBundle() {
        String bundle = "com.mycompany.foo";
        String fileName = getParser().getResourceFileName(key(bundle, "test"));
        assertEquals("com/mycompany/foo.properties", fileName);
    }

}
