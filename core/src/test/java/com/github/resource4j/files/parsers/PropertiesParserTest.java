package com.github.resource4j.files.parsers;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import com.github.resource4j.ResourceObject;
import org.junit.Test;

import com.github.resource4j.generic.objects.ByteArrayResourceObject;

public class PropertiesParserTest {

    @Test
    public void testLoadPropertiesFromString() throws IOException {
        String data ="a=b\nc=d";
        ResourceObject file = new ByteArrayResourceObject("test.properties", "test.properties", data.getBytes(), 0);
        Properties properties = PropertiesParser.getInstance().parse(file);
        assertEquals(2, properties.size());
        assertEquals("b", properties.get("a"));
        assertEquals("d", properties.get("c"));
    }

}
