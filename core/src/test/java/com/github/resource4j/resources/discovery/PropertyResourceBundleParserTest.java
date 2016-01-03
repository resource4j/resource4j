package com.github.resource4j.resources.discovery;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import org.junit.Before;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PropertyResourceBundleParserTest extends AbstractResourceBundleParserTest {

    protected ResourceBundleParser aParser() {
        return new PropertyResourceBundleParser();
    }

    @Override
    protected BundleBuilder aBundle() {
        return new PropertyBundleBuilder();
    }

    protected static class PropertyBundleBuilder extends BundleBuilder {

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
            for (Map.Entry<String,String> entry : values.entrySet()) {
                appendEscaped(content, entry.getKey());
                content.append('=');
                appendEscaped(content, entry.getValue());
                content.append('\n');
            }
            return new ByteArrayResourceObject("test", "test",
                    content.toString().getBytes(StandardCharsets.ISO_8859_1),
                    System.currentTimeMillis());
        }
    }

}
