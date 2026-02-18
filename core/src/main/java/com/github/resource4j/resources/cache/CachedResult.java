package com.github.resource4j.resources.cache;

public sealed interface CachedResult permits CachedValue, CachedBundle, CachedObject {

	boolean exists();

}
