package com.github.resource4j.refreshable;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Objects;

public class ResolvedKey implements java.io.Serializable {

    private ResourceKey key;

    private ResourceResolutionContext context;

    public ResolvedKey(ResourceKey key, ResourceResolutionContext context) {
        this.key = key;
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolvedKey cacheKey = (ResolvedKey) o;
        return Objects.equals(key, cacheKey.key) &&
                Objects.equals(context, cacheKey.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, context);
    }

    @Override
    public String toString() {
        return key.toString() + "@" + context.toString();
    }

    public ResourceResolutionContext context() {
        return context;
    }

    public ResourceKey key() {
        return key;
    }
}
