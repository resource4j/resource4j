package com.github.resource4j.extras.json;

import com.github.resource4j.objects.ByteArrayResourceObject;
import com.github.resource4j.resources.RefreshableResources;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JacksonBundleParserTest {

    private RefreshableResources resources = new RefreshableResources();

    @Test
    public void shouldParseJsonBundle() throws IOException {
        JacksonBundleParser parser = new JacksonBundleParser();
        Map<String, String> map = parser.parse(resources.contentOf("test.json"));
        assertEquals("localhost", map.get("mail.server.host"));
    }

    @Test
    public void shouldParseArrayInJsonBundle() throws IOException {
        JacksonBundleParser parser = new JacksonBundleParser();
        Map<String, String> map = parser.parse(resources.contentOf("test.json"));
        assertNotNull(map.get("items[0]"));
        assertEquals("1", map.get("items[0]"));
    }

    @Test
    public void shouldParseEmptyBundle() throws IOException {
        JacksonBundleParser parser = new JacksonBundleParser();
        Map<String, String> map = parser.parse(new ByteArrayResourceObject("","", "{}".getBytes(), 0));
        assertTrue(map.isEmpty());
    }

}
