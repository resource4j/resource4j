package com.github.resource4j.spring;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.AbstractResourceFile;

public class SpringResourceFile extends AbstractResourceFile {

	private Resource resource;

	public SpringResourceFile(ResourceKey key, Resource resource) {
		super(key, resource.getFilename());
		this.resource = resource;
	}

	@Override
	public InputStream asStream() {
		try {
			return resource.getInputStream();
		} catch (IOException e) {
			throw new InaccessibleResourceException(e);
		}
	}

	@Override
	public boolean exists() {
		return resource.exists();
	}
	
}
