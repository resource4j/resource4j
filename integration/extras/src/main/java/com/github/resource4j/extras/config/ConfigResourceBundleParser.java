package com.github.resource4j.extras.config;

import java.util.Map;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.resources.discovery.ContentType;
import com.github.resource4j.resources.discovery.ResourceBundleParser;

@ContentType(extension = "conf", mimeType = "application/hocon")
public class ConfigResourceBundleParser implements ResourceBundleParser {

	private ConfigMapParser parser = new ConfigMapParser();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<String, String> parse(ResourceObject file) {
        return (Map) file.parsedTo(parser).asIs();
    }


}
