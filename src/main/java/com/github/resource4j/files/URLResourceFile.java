package com.github.resource4j.files;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.parsers.ResourceParser;

public class URLResourceFile implements ResourceFile {

    private ResourceKey key;

    private URL url;

    public URLResourceFile(ResourceKey key, URL url) {
        super();
        if (url == null) throw new NullPointerException("File url cannot be null");
        if (key == null) throw new NullPointerException("Resource file key cannot be null");
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
    public <T,V extends OptionalValue<T>> V parsedTo(ResourceParser<T,V> parser) {
        return parser.parse(key, asStream());
    }

}
