package com.github.resource4j.objects.parsers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

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
