package com.github.resource4j.files.parsers;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.github.resource4j.files.ByteArrayResourceFile;
import com.github.resource4j.files.ResourceFile;

public class PropertiesParserTest {

    @Test
    public void testLoadPropertiesFromString() throws IOException {
        String data ="a=b\nc=d";
        ResourceFile file = new ByteArrayResourceFile(bundle("test.properties"), data.getBytes());
        Properties properties = PropertiesParser.getInstance().parse(file);
        assertEquals(2, properties.size());
        assertEquals("b", properties.get("a"));
        assertEquals("d", properties.get("c"));
    }

}
