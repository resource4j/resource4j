package com.github.resource4j.resources.impl;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.github.resource4j.resources.context.ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR;

public class ResolvedKey implements java.io.Serializable {

    private ResourceKey key;

    private byte paramCount;

    private ResourceResolutionContext context;

    public ResolvedKey(ResourceKey key, ResourceResolutionContext context) {
        this.key = key;
        this.paramCount = (byte) context.parameters().size();
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolvedKey cacheKey = (ResolvedKey) o;
        return (paramCount == cacheKey.paramCount)
                && Objects.equals(key, cacheKey.key)
                && Objects.equals(context, cacheKey.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, paramCount, context);
    }

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
        if (v == null) {
            return "";
        } else if (v instanceof Number) {
            return v.toString();
        } else if (v instanceof Boolean) {
            return v.toString();
        } else if (v instanceof Temporal) {
            return v.toString();
        } else {
            try {
                return URLEncoder.encode(v.toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    public ResourceResolutionContext context() {
        return context;
    }

    public ResourceKey key() {
        return key;
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

    public Object find(Function<ResolvedKey,Optional<Object>> r1,
                                 Function<ResolvedKey,Optional<Object>> r2) {
        return r1.apply(this).orElse(r2.apply(this).orElse(null));
    }

}
