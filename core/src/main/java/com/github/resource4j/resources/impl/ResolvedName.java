package com.github.resource4j.resources.impl;

import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Objects;

public class ResolvedName {

    private String name;

    private ResourceResolutionContext context;

    public ResolvedName(String name, ResourceResolutionContext context) {
        this.name = name;
        this.context = context;
    }

    public String name() {
        return name;
    }

    public ResourceResolutionContext context() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolvedName that = (ResolvedName) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, context);
    }

    @Override
    public String toString() {
        if (context.isEmpty()) {
            return name;
        }

        int idx = name.lastIndexOf('.');
        String path = name;
        String extension = "";
        if (idx >= 0) {
            path = name.substring(0, idx);
            extension = name.substring(idx);
        }
        return path + ResourceResolutionContext.DEFAULT_COMPONENT_SEPARATOR + context.toString() + extension;
    }


}
