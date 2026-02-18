package com.github.resource4j.resources.cache;

import java.util.Map;

public record CachedBundle(String source, Map<String, String> values) implements CachedResult {

    public String get(String id) {
        return values.get(id);
    }

    @Override
    public boolean exists() {
        return values != null;
    }

    @Override
    public String toString() {
        return "bundle " + source + " (" + values.size() + " values)";
    }

}
