package com.github.resource4j.files;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.generic.GenericOptionalValue;

public class URLResourceFile implements ResourceFile {

    private ResourceKey key;

    private URL url;

    public URLResourceFile(ResourceKey key, URL url) {
        super();
        this.key = key;
        this.url = url;
    }

    @Override
    public ResourceKey key() {
        return key;
    }

    @Override
    public InputStream asStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new MissingResourceFileException(key, e);
        }
    }

    @Override
    public <T> OptionalValue<T> parsedTo(ResourceParser<T> parser) {
        T value = parser.parse(asStream());
        return new GenericOptionalValue<T>(key, value);
    }

}
