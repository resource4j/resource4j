package com.github.resource4j.files.lookup;

import static com.github.resource4j.files.parsers.ResourceParsers.properties;

import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;

public class PropertyResourceBundleParser implements ResourceBundleParser {

    private static final String EXTENSION = "properties";

    @Override
    public String getResourceFileName(ResourceKey key) {
        String bundleName = key.getBundle();
        if (bundleName == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bundleName.length() + 1 + EXTENSION.length());
        sb.append(bundleName.replace('.', '/')).append('.').append(EXTENSION);
        return sb.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<String, String> parse(ResourceFile file) {
        return (Map) file.parsedTo(properties()).asIs();
    }

}
