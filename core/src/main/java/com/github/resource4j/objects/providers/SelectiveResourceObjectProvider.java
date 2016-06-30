package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SelectiveResourceObjectProvider implements ResourceObjectProvider, ResourceObjectProviderAdapter {

    private ResourceObjectProvider provider;

    private Predicate<ResourceResolutionContext> contextMatcher = ctx -> true;

    private Predicate<String> nameMatcher = name -> true;

    public SelectiveResourceObjectProvider(ResourceObjectProvider provider,
                                           Predicate<String> nameMatcher,
                                           Predicate<ResourceResolutionContext> contextMatcher) {
        this.provider = provider;
        if (contextMatcher != null) {
            this.contextMatcher = contextMatcher;
        }
        if (nameMatcher != null) {
            this.nameMatcher = nameMatcher;
        }
    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext context) throws ResourceObjectAccessException {
        if (nameMatcher.test(name) && contextMatcher.test(context)) {
            return provider.get(name, context);
        }
        throw new MissingResourceObjectException(name, context.toString());
    }

    @Override
    public ResourceObjectProvider objectsLike(Predicate<String> nameMatcher) {
        this.nameMatcher = this.nameMatcher.or(nameMatcher);
        return this;
    }

    @Override
    public ResourceObjectProvider acceptContext(Predicate<ResourceResolutionContext> contextMatcher) {
        this.contextMatcher = this.contextMatcher.or(contextMatcher);
        return this;
    }

    @Override
    public List<ResourceObjectProvider> unwrap() {
        return provider instanceof ResourceObjectProviderAdapter
                ? ((ResourceObjectProviderAdapter) provider).unwrap()
                : Collections.singletonList(provider);

    }
}
