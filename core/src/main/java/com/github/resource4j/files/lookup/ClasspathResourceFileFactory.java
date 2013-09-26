package com.github.resource4j.files.lookup;

import java.net.URL;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.URLResourceFile;

public class ClasspathResourceFileFactory implements ResourceFileFactory {

    @Override
    public ResourceFile getFile(ResourceKey key, String actualName) throws MissingResourceFileException {
        URL url = getClass().getResource('/'+actualName);
        if (url == null) throw new MissingResourceFileException(key, actualName);
        return new URLResourceFile(key, url);
    }

}
