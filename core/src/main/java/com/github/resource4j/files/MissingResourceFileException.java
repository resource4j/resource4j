package com.github.resource4j.files;

import java.io.IOException;

import com.github.resource4j.ResourceKey;

public class MissingResourceFileException extends ResourceFileException {

    private static final long serialVersionUID = 1L;

    public MissingResourceFileException(ResourceKey key) {
        super(key);
    }

    public MissingResourceFileException(ResourceKey key, String actualFileName) {
        super(key, actualFileName);
    }

    public MissingResourceFileException(ResourceKey key, IOException reason) {
        super(key, reason);
    }


}
