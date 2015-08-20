package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.util.Map;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;

public class KeyValueParser extends AbstractValueParser<Map<String,String>> {
    
	private static final KeyValueParser INSTANCE = new KeyValueParser();
    
	public static KeyValueParser getInstance() {
		return INSTANCE;
	}
	
	private PropertiesParser delegate = PropertiesParser.getInstance();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Map<String, String> parse(ResourceFile file) throws IOException, ResourceFileException {
		return (Map) delegate.parse(file);
	}

}
