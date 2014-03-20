package com.github.resource4j.spring;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.parsers.ResourceParser;

public class SpringResourceFile implements ResourceFile {

	private ResourceKey key;
	private Resource resource;

	public SpringResourceFile(ResourceKey key, Resource resource) {
		this.key = key;
		this.resource = resource;
	}

	@Override
	public ResourceKey key() {
		return key;
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
	public <T, V extends OptionalValue<T>> V parsedTo(ResourceParser<T, V> parser) {
		return parser.parse(key, asStream());
	}

	@Override
	public String toString() {
		return resource.getFilename();
	}
	
}
