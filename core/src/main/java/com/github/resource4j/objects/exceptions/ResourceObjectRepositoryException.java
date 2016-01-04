package com.github.resource4j.objects.exceptions;

import com.github.resource4j.ResourceObjectException;


public class ResourceObjectRepositoryException extends ResourceObjectException {

    public ResourceObjectRepositoryException(String message, String name, String actualName) {
        super(message, name, actualName);
    }

    public ResourceObjectRepositoryException(String message, Throwable cause, String name, String actualName) {
        super(name, actualName, message, cause);
    }

}
