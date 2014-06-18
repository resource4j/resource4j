package com.github.resource4j.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.parsers.ResourceParser;

public class ByteArrayResourceFile implements ResourceFile {

	private ResourceKey key;
	
	private byte[] value;
	
	public ByteArrayResourceFile(ResourceKey key, byte[] value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public ResourceKey key() {
		return key;
	}

	@Override
	public String resolvedName() {
		return key.getBundle();
	}

	@Override
	public InputStream asStream() {
		return new ByteArrayInputStream(value);
	}

	@Override
	public <T, V extends OptionalValue<T>> V parsedTo(
			ResourceParser<T, V> parser) {
		return parser.parse(key, this);
	}

}
