package com.github.resource4j;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.processors.CyclicReferenceException;
import com.github.resource4j.resources.processors.ValuePostProcessingException;

/**
 * Abstract superclass for all resource resolution related exceptions.
 * @author Ivan Gammel
 * @see Resources
 */
public abstract sealed class ResourceException extends RuntimeException
        permits MissingValueException, ValueNotAcceptableException, ResourceObjectException,
                CyclicReferenceException, ValuePostProcessingException {

    private static final long serialVersionUID = 1L;

    protected ResourceException() {
        super();
    }

    protected ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ResourceException(String message) {
        super(message);
    }

    protected ResourceException(Throwable cause) {
        super(cause);
    }

}
