package com.github.resource4j.objects.parsers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.resources.discovery.ContentType;

import java.io.IOException;
import java.util.Map;

@ContentType(extension = ".properties", mimeType = "text/x-java-properties")
public class KeyValueParser extends AbstractValueParser<Map<String,String>> implements BundleParser {
    
	private static final KeyValueParser INSTANCE = new KeyValueParser();
    
	public static KeyValueParser getInstance() {
		return INSTANCE;
	}
	
	private PropertiesParser delegate = PropertiesParser.getInstance();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Map<String, String> parse(ResourceObject object) throws IOException, ResourceObjectException {
		return (Map) delegate.parse(object);
	}

}
