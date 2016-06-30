package com.github.resource4j.resources.discovery;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.test.Builder;

import java.util.HashMap;
import java.util.Map;

public abstract class BundleBuilder implements Builder<ResourceObject> {

    protected final Map<String, String> values = new HashMap<>();

    public BundleBuilder with(String key, String value) {
        values.put(key, value);
        return this;
    }

    public abstract ResourceObject build();

}
