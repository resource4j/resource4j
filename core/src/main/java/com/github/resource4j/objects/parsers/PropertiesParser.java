package com.github.resource4j.objects.parsers;

import com.github.resource4j.ResourceObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser extends AbstractValueParser<Properties> {

    private static final PropertiesParser INSTANCE = new PropertiesParser();

    @Override
    protected Properties parse(ResourceObject object) throws IOException, ResourceObjectFormatException {
    	try (InputStream stream = object.asStream()) {
	        Properties properties = new Properties();
	        properties.load(stream);
	        return properties;
    	} catch (IllegalArgumentException e) {
    		throw new ResourceObjectFormatException(object, "{0} - " + e.getMessage());
    	}
    }

    public static PropertiesParser getInstance() {
        return INSTANCE;
    }

}
