package com.github.resource4j.refreshable.cache;

import java.io.Serializable;
import java.util.Map;

public class CachedBundle implements Serializable, CachedResult {

    private String source;

    private Map<String,String> values;

    public CachedBundle(String source, Map<String,String> values) {
        this.source = source;
        this.values = values;
    }

    public String source() {
        return this.source;
    }

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
