package com.github.resource4j.objects.exceptions;

import com.github.resource4j.ResourceObjectException;

public class ResourceObjectAccessException extends ResourceObjectException {

    public ResourceObjectAccessException(String name) {
        super(name);
    }

    public ResourceObjectAccessException(String name, String actualName) {
        super(name, actualName);
    }

    public ResourceObjectAccessException(Throwable cause, String name) {
        super(cause, name);
    }

    public ResourceObjectAccessException(Throwable cause, String name, String actualName) {
        super(cause, name, actualName);
    }

    public ResourceObjectAccessException(String message, String name, String actualName) {
        super(message, name, actualName);
    }

}
