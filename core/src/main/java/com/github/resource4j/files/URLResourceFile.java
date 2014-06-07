package com.github.resource4j.files;

import java.io.FileNotFoundException;
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
	public String resolvedName() {
		return url.toString();
	}

	@Override
    public InputStream asStream() {
        try {
            return url.openStream();
        } catch (FileNotFoundException e) {
            throw new MissingResourceFileException(key, e);
        } catch (IOException e) {
        	throw new InaccessibleResourceException(this, e);
        }
    }

    @Override
    public <T,V extends OptionalValue<T>> V parsedTo(ResourceParser<T,V> parser) {
        return parser.parse(key, this);
    }

    @Override
    public String toString() {
    	return resolvedName();
    }
    
}
