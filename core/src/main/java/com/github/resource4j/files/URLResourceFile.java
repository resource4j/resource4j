package com.github.resource4j.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.github.resource4j.ResourceKey;

public class URLResourceFile extends AbstractResourceFile {

    private ResourceKey key;

    private URL url;

    public URLResourceFile(ResourceKey key, URL url) {
        super(key, validateUrl(url));
        this.url = url;
    }

    private static String validateUrl(URL url) {
        if (url == null) throw new NullPointerException("File url cannot be null");
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
	public boolean exists() {
		try {
			InputStream stream = url.openStream();
			if (stream != null) {
				stream.close();
			} 
			return true;
        } catch (IOException e) {
        	return false;
        }
	}
    
}
