package com.github.resource4j.generic.objects.factory;

import java.io.File;

import com.github.resource4j.MissingResourceObjectException;
import com.github.resource4j.generic.objects.FileResourceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileResourceObjectFactory implements ResourceObjectFactory {

	private static final Logger LOG = LoggerFactory.getLogger(FileResourceObjectFactory.class);
	
	private File base;

	public FileResourceObjectFactory(String basePath) {
		this(new File(basePath));
	}
	
	public FileResourceObjectFactory(File base) {
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
	public FileResourceObject getObject(String name, String actualName) throws MissingResourceObjectException {
		File file = new File(base, actualName);
		LOG.trace("Requested file: {}", file.getAbsolutePath());
		if (!file.exists() || file.isDirectory()) {
			throw new MissingResourceObjectException(name, actualName);
		}
		return new FileResourceObject(name, file);
	}

}
