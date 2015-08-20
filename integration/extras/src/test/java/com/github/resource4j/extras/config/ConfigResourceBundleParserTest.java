package com.github.resource4j.extras.config;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.URLResourceFile;

public class ConfigResourceBundleParserTest {
	
	@Test
	public void testGetResourceFileName() throws Exception {
		ConfigResourceBundleParser parser = new ConfigResourceBundleParser();
		String name = parser.getResourceFileName(bundle("a.b.c"));
		assertEquals("a/b/c.conf", name);
	}
	
	@Test
	public void testParseConfig() throws Exception {
		ResourceKey key = bundle("test");

		ConfigResourceBundleParser parser = new ConfigResourceBundleParser();
		
		String name = parser.getResourceFileName(key);
		ResourceFile file = new URLResourceFile(key, getClass().getResource("/" + name));
		
		Map<String, String> map = parser.parse(file);
		
		assertNotNull(map);
		assertEquals(5, map.size());
		
		assertEquals("25", map.get("mail.server.port"));
		assertEquals("localhost", map.get("mail.server.host"));
	}
	
}
