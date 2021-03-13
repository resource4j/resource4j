package com.github.resource4j.extras.config;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.URIResourceObject;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Map;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigResourceBundleParserTest {

	@Test
	public void testParseConfig() throws Exception {
		ResourceKey key = bundle("test");
		ConfigResourceBundleParser parser = new ConfigResourceBundleParser();
		
		String name = "test.conf";
		String resolvedName = "/" + name;
		URL url = getClass().getResource(resolvedName);
		ResourceObject file = new URIResourceObject(url.toURI(), key.getBundle(), resolvedName, 0);
		
		Map<String, String> map = parser.parse(file);
		
		assertNotNull(map);
		assertEquals(5, map.size());
		
		assertEquals("25", map.get("mail.server.port"));
		assertEquals("localhost", map.get("mail.server.host"));
	}
	
}
