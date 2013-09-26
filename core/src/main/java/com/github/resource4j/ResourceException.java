package com.github.resource4j;

public abstract class ResourceException extends RuntimeException {

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
