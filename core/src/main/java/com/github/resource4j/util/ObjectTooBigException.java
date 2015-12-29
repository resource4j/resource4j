package com.github.resource4j.util;

import java.io.IOException;

public class ObjectTooBigException extends IOException {

    static final long serialVersionUID = 7818375828146090155L;

    public ObjectTooBigException(long limit) {
        super("Requested object is bigger than " + limit + " bytes");
    }

}
