package com.github.resource4j.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.resource4j.ResourceKey;

public class ByteArrayResourceFile extends AbstractResourceFile {
	
	private byte[] value;
	
	public ByteArrayResourceFile(ResourceKey key, byte[] value) {
		super(key, key.getBundle());
		this.value = value;
	}

	@Override
	public InputStream asStream() {
		return new ByteArrayInputStream(value);
	}

	/**
	 * Always returns <code>true</code>
	 */
	@Override
	public boolean exists() {
		return true;
	}

}
