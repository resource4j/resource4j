package com.github.resource4j.resources.cache;

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
			return object != null && object.size() >= 0;
		} catch (InaccessibleResourceObjectException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return "object " + (object != null ? object.actualName() : "<missing>");
	}

}
