package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.function.Predicate;

/**
 * Common interface for resource object providers.
 */
public interface ResourceObjectProvider {

    /**
     * Returns resource object with given resolved name or throws exception if object does not exist or is not
     * accessible.
     * @param name the name of the object requested by user that does not include the resolution context
     * @param context to resolve the object name in
     * @return existing resource object (not null)
     * @throws com.github.resource4j.objects.exceptions.ResourceObjectAccessException if object does not exist, is not accessible or not readable.
     */
    ResourceObject get(String name, ResourceResolutionContext context) throws ResourceObjectAccessException;

    /**
     * Returns provider that accepts requests only for objects which name matches given condition.
     * @param nameMatcher name matching condition
     * @return adapter that filters object requests, allowing to return only objects with matching names.
     */
    default ResourceObjectProvider objectsLike(Predicate<String> nameMatcher) {
        return new SelectiveResourceObjectProvider(this, nameMatcher, null);
    }

    /**
     * Returns provider that accepts requests with resolution contexts matching given condition.
     * @param contextMatcher context matching condition
     * @return adapter that filters object requests, allowing to return only objects in matching contexts.
     */
    default ResourceObjectProvider acceptContext(Predicate<ResourceResolutionContext> contextMatcher) {
        return new SelectiveResourceObjectProvider(this, null, contextMatcher);
    }

}
