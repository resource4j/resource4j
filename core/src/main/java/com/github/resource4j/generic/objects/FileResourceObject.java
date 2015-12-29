package com.github.resource4j.generic.objects;

import com.github.resource4j.InaccessibleResourceException;
import com.github.resource4j.MissingResourceObjectException;

import java.io.*;
import java.time.Instant;

public class FileResourceObject extends AbstractResourceObject {

    private File file;

    public FileResourceObject(String name, File file) {
        super(name, file.getAbsolutePath());
        this.file = file;
    }

    @Override
    public InputStream asStream() throws InaccessibleResourceException {
        try {
            return new FileInputStream(file);
        } catch (IOException | SecurityException e) {
            throw new InaccessibleResourceException(e, name, resolvedName);
        }
    }

    @Override
    public long size() throws InaccessibleResourceException {
        if (!file.exists() || file.isDirectory()) {
            throw new InaccessibleResourceException("File become inaccessible: size cannot be calculated",
                    name,
                    resolvedName);
        }
        try {
            return file.length();
        } catch (SecurityException e) {
            throw new InaccessibleResourceException(e, name, resolvedName);
        }
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

}
