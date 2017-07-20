package com.github.resource4j.resources.impl;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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
        return key.toString() + "(" + paramCount + ")"
                + (context.isEmpty()
                    ? ""
                    : ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR + context.toString());
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
