package com.github.resource4j.objects;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.objects.parsers.ResourceParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayResourceObject extends AbstractResourceObject {
	
	private byte[] value;

	private long timestamp;

	public ByteArrayResourceObject(String name, String resolvedName, byte[] value, long timestamp) {
		super(name, resolvedName);
		this.value = value;
        this.timestamp = timestamp;
	}

	@Override
	public InputStream asStream() {
		return new ByteArrayInputStream(value);
	}

	@Override
	public long size() {
		return value.length;
	}

    @Override
    public long lastModified() {
        return timestamp;
    }

    @Override
    public <T> OptionalValue<T> parsedTo(ResourceParser<T, ? extends OptionalValue<T>> parser) {
        return parser.parse(key(), this);
    }

}
