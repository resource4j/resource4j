package com.github.resource4j.objects.exceptions;

public class InvalidResourceObjectException extends ResourceObjectAccessException {

    private static final long serialVersionUID = 1;

    public InvalidResourceObjectException(String message, String name, String actualName) {
        super(message, name, actualName);
    }
}
