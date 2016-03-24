package com.github.resource4j.resources.discovery;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PropertyBundleBuilder extends BundleBuilder {

    private String name = "test";

    private String resolvedName = "test";

    public static PropertyBundleBuilder aPropertiesBundle() {
        return new PropertyBundleBuilder();
    }

    public static PropertyBundleBuilder aPropertiesBundle(String name, String resolvedName) {
        return new PropertyBundleBuilder(name, resolvedName);
    }

    public PropertyBundleBuilder() {
    }

    public PropertyBundleBuilder(String name, String resolvedName) {
        this.name = name;
        this.resolvedName = resolvedName;
    }

    private static void appendEscaped(StringBuilder builder, String unicodeText) {
        for (char c : unicodeText.toCharArray()) {
            if (c >= 128)
                builder.append("\\u").append(String.format("%04X", (int) c));
            else
                builder.append(c);
        }
    }

    @Override
    public ResourceObject build() {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            appendEscaped(content, entry.getKey());
            content.append('=');
            appendEscaped(content, entry.getValue());
            content.append('\n');
        }
        return new ByteArrayResourceObject(name, resolvedName,
                content.toString().getBytes(StandardCharsets.ISO_8859_1),
                System.currentTimeMillis());
    }
}
