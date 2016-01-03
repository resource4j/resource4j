package com.github.resource4j.resources.discovery;

import static com.github.resource4j.objects.parsers.ResourceParsers.properties;

import java.util.Map;

import com.github.resource4j.ResourceObject;

@ContentType(extension = "properties", mimeType = "text/x-java-properties")
public class PropertyResourceBundleParser implements ResourceBundleParser {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<String, String> parse(ResourceObject file) {
        return (Map) file.parsedTo(properties()).asIs();
    }

}
