package com.github.resource4j.resources.impl;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.github.resource4j.resources.context.ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR;

public record ResolvedKey(ResourceKey key, ResourceResolutionContext context) implements java.io.Serializable {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(key.toString());
        if (!context.parameters().isEmpty()) {
            context.parameters().forEach((k, v) -> builder.append(';').append(k).append('=').append(escaped(v)));
        }
        if (!context.isEmpty()) {
            builder.append(DEFAULT_COMPONENT_SEPARATOR).append(context);
        }
        return builder.toString();
    }

    private String escaped(Object v) {
        return switch (v) {
            case null -> "";
            case Number _ -> v.toString();
            case Boolean _ -> v.toString();
            case Temporal _ -> v.toString();
            default -> URLEncoder.encode(v.toString(), StandardCharsets.UTF_8);
        };
    }

    public ResolvedKey relative(String id, Map<String, Object> params) {
        return new ResolvedKey(key.relative(id), context.merge(params));
    }

    public Map<String, Object> params() {
        return context.parameters();
    }

    public String id() {
        return key.getId();
    }

    public Object find(Function<ResolvedKey, Optional<Object>> r1,
                       Function<ResolvedKey, Optional<Object>> r2) {
        return r1.apply(this).orElse(r2.apply(this).orElse(null));
    }

}
