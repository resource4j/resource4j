package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Arrays;
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
        String realName = basePath + (name.startsWith("/") ? name : '/' + name);
        return provider.get(realName, context);
    }

    public List<ResourceObjectProvider> unwrap() {
        return provider instanceof ResourceObjectProviderAdapter
                ? ((ResourceObjectProviderAdapter) provider).unwrap()
                : Collections.singletonList(provider);
    }

}
