package com.github.resource4j.resources.cache;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public class SimpleResourceCacheKey {
	
	private ResourceResolutionContext resolutionContext;
	
	private ResourceKey resourceKey;

	public SimpleResourceCacheKey(ResourceResolutionContext resolutionContext,
			ResourceKey resourceKey) {
		super();
		this.resolutionContext = resolutionContext;
		this.resourceKey = resourceKey;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((resolutionContext == null) ? 0 : resolutionContext
						.hashCode());
		result = prime * result
				+ ((resourceKey == null) ? 0 : resourceKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
	SimpleResourceCacheKey other = (SimpleResourceCacheKey) obj;
		if (resolutionContext == null) {
			if (other.resolutionContext != null)
				return false;
		} else if (!resolutionContext.equals(other.resolutionContext))
			return false;
		if (resourceKey == null) {
			if (other.resourceKey != null)
				return false;
		} else if (!resourceKey.equals(other.resourceKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return resolutionContext + ":" + resourceKey;
	}
	
}