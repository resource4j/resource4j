package com.github.resource4j.resources.references;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class GenericResourceValueReference implements ResourceValueReference {

	private Resources resources;
	
	private ResourceKey key;
	
	public GenericResourceValueReference(Resources resources, ResourceKey key) {
		super();
		if (resources == null) {
			throw new NullPointerException("resources");
		}
		if (key == null) {
			throw new NullPointerException("key");
		}
		this.resources = resources;
		this.key = key;
	}

	@Override
	public ResourceKey key() {
		return key;
	}

	@Override
	public OptionalString fetch(ResourceResolutionContext context) {
		return resources.get(key, context);
	}

	@Override
	public OptionalString in(Object... components) {
		return fetch(ResourceResolutionContext.in(components));
	}

}
