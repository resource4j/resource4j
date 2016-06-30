package com.github.resource4j.objects;

import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;

import java.io.*;

public class FileResourceObject extends AbstractResourceObject {

    private File file;

    public FileResourceObject(String name, File file) {
        super(name, file.getAbsolutePath());
        this.file = file;
    }

    @Override
    public InputStream asStream() throws InaccessibleResourceObjectException {
        try {
            return new FileInputStream(file);
        } catch (IOException | SecurityException e) {
            throw new InaccessibleResourceObjectException(e, name, resolvedName);
        }
    }

    @Override
    public long size() throws InaccessibleResourceObjectException {
        if (!file.exists() || file.isDirectory()) {
            throw new InaccessibleResourceObjectException("File become inaccessible: size cannot be calculated",
                    name,
                    resolvedName);
        }
        try {
            return file.length();
        } catch (SecurityException e) {
            throw new InaccessibleResourceObjectException(e, name, resolvedName);
        }
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

}
