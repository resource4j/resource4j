package com.github.resource4j.objects.exceptions;

import com.github.resource4j.ResourceObjectException;

public class InvalidResourceObjectException extends ResourceObjectAccessException {

    public InvalidResourceObjectException(String message, String name, String actualName) {
        super(message, name, actualName);
    }
}
