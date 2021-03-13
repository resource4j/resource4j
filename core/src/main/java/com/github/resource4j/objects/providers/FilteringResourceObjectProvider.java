package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Collections;
import java.util.List;

public class FilteringResourceObjectProvider implements ResourceObjectProvider, ResourceObjectProviderAdapter {

    private ResourceObjectProvider provider;

    private String basePath;

    public FilteringResourceObjectProvider(ResourceObjectProvider provider, String basePath) {
        this.provider = provider;
        this.basePath = basePath;
    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext context) throws ResourceObjectAccessException {
        int idx = name.indexOf(':');
        String schema = idx >= 0 ? name.substring(0, idx + 1) : "";
        String path = idx >= 0 ? name.substring(idx + 1) : name;
        String realName = schema + basePath + (path.startsWith("/") ? path : '/' + path);
        return provider.get(realName, context);
    }

    public List<ResourceObjectProvider> unwrap() {
        return provider instanceof ResourceObjectProviderAdapter
                ? ((ResourceObjectProviderAdapter) provider).unwrap()
                : Collections.singletonList(provider);
    }

}
