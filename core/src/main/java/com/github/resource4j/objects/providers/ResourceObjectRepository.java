package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import com.github.resource4j.util.IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Common interface for mutable resource object providers, e.g. the ones working with database.
 */
public interface ResourceObjectRepository extends ResourceObjectProvider {

    /**
     * Checks if the object with given resolved name is present in this repository
     * @param resolvedName  resolved name of the object to check existence
     * @return <code>true</code> if object exists in the repository, is accessible and is readable, <code>false</code> otherwise.
     * @throws ResourceObjectRepositoryException if requested object existence cannot be determined for some reason
     * unrelated to this specific object.
     */
    boolean contains(String resolvedName) throws ResourceObjectRepositoryException;

    /**
     * Stores data provided by given supplier as resource object with given resolved name
     * @param name name of resource object that will be requested by client
     * @param resolvedName name of the stored object resolved within resolution context
     * @param datasource the source of stored data
     * @throws java.io.IOException if failed to read the data from the given datasource.
     * @throws ResourceObjectAccessException if resource object in repository cannot be created or it exists, but
     * is not accessible or not writable
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    default void put(String name, String resolvedName, Supplier<InputStream> datasource)
            throws ResourceObjectAccessException, ResourceObjectRepositoryException, IOException {
        try (InputStream inputStream = datasource.get()) {
            byte[] data = IO.read(inputStream);
            put(name, resolvedName, data);
        }
    }

    /**
     * Stores given binary data as resource object with given resolved name
     * @param name name of resource object that will be requested by client
     * @param resolvedName name of the stored object resolved within resolution context
     * @param data the source of stored data
     * @throws ResourceObjectAccessException if resource object in repository cannot be created or it exists, but
     * is not accessible or not writable
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    void put(String name, String resolvedName, byte[] data)
            throws ResourceObjectAccessException, ResourceObjectRepositoryException;

    /**
     * Remove object with given resolved name from the repository. Does nothing if object is already not in
     * this repository.
     * @param resolvedName the resolved name of removed object
     * @throws ResourceObjectAccessException if resource object in repository exists, but cannot be deleted
     * @throws ResourceObjectRepositoryException if requested operation cannot be performed for other reasons
     * unrelated to this specific object.
     */
    void remove(String resolvedName) throws ResourceObjectRepositoryException, ResourceObjectAccessException;

}
