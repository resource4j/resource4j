package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser extends AbstractValueParser<Properties> {

    private static final PropertiesParser INSTANCE = new PropertiesParser();

    @Override
    protected Properties parse(InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

    public static PropertiesParser getInstance() {
        return INSTANCE;
    }

}
