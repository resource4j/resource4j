package com.github.resource4j.resources.cache;

public class CachedValue implements CachedResult {

	String value;

	String source;

	public CachedValue(String value, String source) {
		this.value = value;
		this.source = source;
	}

	public boolean exists() {
		return value != null;
	}

	public String value() {
		return value;
	}

	public String source() {
		return source;
	}

	@Override
	public String toString() {
		return value != null ? String.valueOf(value) + " @ " + source : "<missing>";
	}

}
