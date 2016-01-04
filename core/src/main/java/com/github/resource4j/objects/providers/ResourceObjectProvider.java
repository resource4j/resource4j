package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;

/**
 * Common interface for resource object providers.
 */
public interface ResourceObjectProvider {

    /**
     * Returns resource object with given resolved name or throws exception if object does not exist or is not
     * accessible.
     * @param name the name of the object requested by user that does not include the resolution context
     * @param resolvedName the real name of the object based on resolution context
     * @return existing resource object (not null)
     * @throws com.github.resource4j.objects.exceptions.ResourceObjectAccessException if object does not exist, is not accessible or not readable.
     */
    ResourceObject get(String name, String resolvedName) throws ResourceObjectAccessException;
    
}
