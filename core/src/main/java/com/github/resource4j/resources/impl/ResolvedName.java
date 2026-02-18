package com.github.resource4j.resources.impl;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public record ResolvedName(String name, ResourceResolutionContext context) {

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
