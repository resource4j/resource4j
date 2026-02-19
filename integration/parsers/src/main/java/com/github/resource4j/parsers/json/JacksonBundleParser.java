package com.github.resource4j.parsers.json;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.parsers.AbstractValueParser;
import com.github.resource4j.objects.parsers.BundleParser;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.resource4j.parsers.json.JacksonParser.json;

public class JacksonBundleParser extends AbstractValueParser<Map<String, String>> implements BundleParser {

    private static final String DEFAULT_ARRAY_SIZE_KEY = "length";

    private String arraySizeKey = DEFAULT_ARRAY_SIZE_KEY;

    public JacksonBundleParser() {
    }

    public JacksonBundleParser(String arraySizeKey) {
        this.arraySizeKey = arraySizeKey;
    }

    @Override
    protected Map<String, String> parse(ResourceObject object) throws IOException, ResourceObjectException {
        Map<String, String> bundle = new HashMap<>();
        processNode(bundle, json().parse(object), "");
        return bundle;
    }

    private void processNode(Map<String, String> bundle, JsonNode current, String key) {
        if (current.isValueNode()) {
            bundle.put(key, current.asString());
        } else if (current.getNodeType() == JsonNodeType.OBJECT) {
            current.properties().forEach(entry -> {
                String childKey = (!key.isEmpty() ? key + "." : "") + entry.getKey();
                processNode(bundle, entry.getValue(), childKey);
            });
        } else if (current.getNodeType() == JsonNodeType.ARRAY) {
            bundle.put(key + ResourceKey.ID_COMPONENT_DELIMITER + arraySizeKey, String.valueOf(current.size()));
            for (int i = 0; i < current.size(); i++) {
                processNode(bundle, current.get(i), key + "[" + i + "]");
            }
        }
    }

}
