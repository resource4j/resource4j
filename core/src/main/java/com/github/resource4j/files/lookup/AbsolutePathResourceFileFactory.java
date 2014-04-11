package com.github.resource4j.files.lookup;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.URLResourceFile;

public class AbsolutePathResourceFileFactory implements ResourceFileFactory {

	private static final Logger LOG = LoggerFactory.getLogger(AbsolutePathResourceFileFactory.class);
	
	private File base;

	public AbsolutePathResourceFileFactory(String basePath) {
		this(new File(basePath));
	}
	
	public AbsolutePathResourceFileFactory(File base) {
		if (!base.exists()) {
			throw new IllegalArgumentException("Resource path does not exist: " + base.getAbsolutePath());
		}
		if (!base.isDirectory()) {
			throw new IllegalArgumentException("Resource path is not directory: " + base.getAbsolutePath());
		}
		this.base = base;
		LOG.debug("Resource path configured: " + base.getAbsolutePath());
	}

	@Override
	public ResourceFile getFile(ResourceKey key, String actualName) throws MissingResourceFileException {
		try {
			File file = new File(base, actualName);
			LOG.trace("Requested file: {}", file.getAbsolutePath());
			if (!file.exists() || file.isDirectory()) {
				throw new MissingResourceFileException(key, actualName); 
			}
			URL url = file.toURI().toURL();
	        return new URLResourceFile(key, url);
		} catch (MalformedURLException reason) {
			throw new MissingResourceFileException(key, reason);
		}
	}

}
