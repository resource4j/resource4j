package com.github.resource4j;

import java.io.IOException;

public class MissingResourceObjectException extends ResourceObjectException {

    private static final long serialVersionUID = 1L;

    public MissingResourceObjectException(String name) {
        super(name);
    }

    public MissingResourceObjectException(String name, String actualName) {
        super(name, actualName);
    }

    public MissingResourceObjectException(Throwable reason, String name) {
        super(reason, name);
    }

    public MissingResourceObjectException(Throwable cause, String name, String actualName) {
        super(cause, name, actualName);
    }

    public MissingResourceObjectException(String message, String name, String actualName) {
        super(message, name, actualName);
    }

}
