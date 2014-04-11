package com.github.resource4j.files.lookup;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.URLResourceFile;

public class ClasspathResourceFileFactory implements ResourceFileFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClasspathResourceFileFactory.class);
	
	public ClasspathResourceFileFactory() {
		LOG.debug("Resource path configured: <classpath>");
	}
	
    @Override
    public ResourceFile getFile(ResourceKey key, String actualName) throws MissingResourceFileException {
        URL url = getClass().getResource('/'+actualName);
        if (url == null) throw new MissingResourceFileException(key, actualName);
        return new URLResourceFile(key, url);
    }

}
