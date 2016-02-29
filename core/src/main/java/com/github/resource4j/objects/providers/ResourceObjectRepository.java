package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.util.IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Common interface for mutable resource object providers, e.g. the ones working with database.
 */
public interface ResourceObjectRepository extends ResourceObjectProvider {

    /**
     * Checks if the object with given name is present in given context in this repository
     * @param name the name of the object to check existence of
     * @param context the resolution context in which the existence of the object will be checked
     * @return <code>true</code> if object exists in the repository, is accessible and is readable, <code>false</code> otherwise.
     * @throws ResourceObjectRepositoryException if requested object existence cannot be determined for some reason
     * unrelated to this specific object.
     */
    boolean contains(String name, ResourceResolutionContext context) throws ResourceObjectRepositoryException;

    /**
     * Stores data provided by given supplier as resource object with given resolved name
     * @param name name of resource object that will be requested by client
     * @param context the resolution context in which the resoource will be created
     * @param datasource the source of stored data
     * @throws java.io.IOException if failed to read the data from the given datasource.
     * @throws ResourceObjectAccessException if resource object in repository cannot be created or it exists, but
     * is not accessible or not writable
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    default void put(String name, ResourceResolutionContext context, Supplier<InputStream> datasource)
            throws ResourceObjectAccessException, ResourceObjectRepositoryException, IOException {
        try (InputStream inputStream = datasource.get()) {
            byte[] data = IO.read(inputStream);
            put(name, context, data);
        }
    }

    /**
     * Stores given binary data as resource object with given resolved name
     * @param name name of resource object that will be requested by client
     * @param context the resolution context in which resource will be created
     * @param data the source of stored data
     * @throws ResourceObjectAccessException if resource object in repository cannot be created or it exists, but
     * is not accessible or not writable
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    void put(String name, ResourceResolutionContext context, byte[] data)
            throws ResourceObjectAccessException, ResourceObjectRepositoryException;

    /**
     * Remove object with given resolved name from the repository. Does nothing if object is already not in
     * this repository.
     * @param name the name of resource to remove from repository
     * @param context the context in which the resource will be removed
     * @throws ResourceObjectAccessException if resource object in repository exists, but cannot be deleted
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    void remove(String name, ResourceResolutionContext context) throws ResourceObjectRepositoryException, ResourceObjectAccessException;

}
