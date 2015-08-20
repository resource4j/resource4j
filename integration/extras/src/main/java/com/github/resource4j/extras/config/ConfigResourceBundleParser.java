package com.github.resource4j.extras.config;

import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.lookup.ResourceBundleParser;

public class ConfigResourceBundleParser implements ResourceBundleParser {

    private static final String EXTENSION = "conf";
    
	private ConfigMapParser parser = new ConfigMapParser();

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
        return (Map) file.parsedTo(parser).asIs();
    }


}
