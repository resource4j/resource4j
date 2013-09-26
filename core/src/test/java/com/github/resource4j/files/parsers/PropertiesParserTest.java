package com.github.resource4j.files.parsers;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class PropertiesParserTest {

    @Test
    public void testLoadPropertiesFromString() throws IOException {
        String data ="a=b\nc=d";
        Properties properties = PropertiesParser.getInstance().parse(new ByteArrayInputStream(data.getBytes()));
        assertEquals(2, properties.size());
        assertEquals("b", properties.get("a"));
        assertEquals("d", properties.get("c"));
    }

}
