package com.github.resource4j.refreshable.cache;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;

public class CachedObject implements CachedResult {

	private ResourceObject object;

	public CachedObject(ResourceObject object) {
		this.object = object;
	}

	public ResourceObject get() {
		return this.object;
	}

	@Override
	public boolean exists() {
		try {
			return object.size() >= 0;
		} catch (InaccessibleResourceObjectException e) {
			return false;
		}
	}

}
