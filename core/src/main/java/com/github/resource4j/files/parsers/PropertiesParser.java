package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.util.Properties;

import com.github.resource4j.files.ResourceFile;

public class PropertiesParser extends AbstractValueParser<Properties> {

    private static final PropertiesParser INSTANCE = new PropertiesParser();

    @Override
    protected Properties parse(ResourceFile file) throws IOException, ResourceFileFormatException {
    	try {
	        Properties properties = new Properties();
	        properties.load(file.asStream());
	        return properties;
    	} catch (IllegalArgumentException e) {
    		throw new ResourceFileFormatException(file, "{0} - " + e.getMessage());
    	}
    }

    public static PropertiesParser getInstance() {
        return INSTANCE;
    }

}
