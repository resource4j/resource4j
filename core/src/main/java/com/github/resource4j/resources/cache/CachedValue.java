package com.github.resource4j.resources.cache;

public record CachedValue(String value, String source) implements CachedResult {

	@Override
	public boolean exists() {
		return value != null;
	}

	@Override
	public String toString() {
		return value != null ? String.valueOf(value) + " @ " + source : "<missing>";
	}

}
