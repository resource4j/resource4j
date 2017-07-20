package com.github.resource4j.resources.params;

import com.github.resource4j.converters.TypeConverter;
import com.github.resource4j.objects.exceptions.InvalidResourceObjectException;
import com.github.resource4j.resources.ParametrizedKeyBuilder;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.impl.ResolvedKey;

public class LocalizedParamKeyBuilder implements ParametrizedKeyBuilder {

    @Override
    public String forResolvedKey(ResolvedKey key, String declaration) {
        String[] params = declaration.split(" ");
        StringBuilder id = new StringBuilder();
        for (String param : params) {
            String name = param;
            int idx = param.indexOf(':');
            if (idx > 0) {
                name = param.substring(0, idx);
            }
            Object value = key.params().get(name);
            if (idx > 0) {
                if (idx == param.length()) {
                    throw new IllegalArgumentException("Invalid declaration format");
                }
                String property = param.substring(idx + 1);

                if ("category".equals(property)) {
                    long number = TypeConverter.convert(value, Long.class);
                    // TODO: add locale-specific rules here
                    if (number % 10 == 1) {
                        value = "singular";
                    } else {
                        value = "plural";
                    }
                }
            }
            id.append('.').append(name).append('.').append(value);
        }
        return id.toString();
    }

    @Override
    public String forDeclaredParams(ResolvedKey key) {
        return key.id() + ".count";
    }


}
