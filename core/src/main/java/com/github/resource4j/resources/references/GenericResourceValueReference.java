package com.github.resource4j.resources.references;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class GenericResourceValueReference implements ResourceValueReference {

	private ResourceProvider provider;
	
	private String name;
	
	public GenericResourceValueReference(Resources resources, ResourceKey key) {
		super();
		if (resources == null) {
			throw new NullPointerException("resources");
		}
		if (key == null) {
			throw new NullPointerException("key");
		}
		this.provider = resources.forKey(key.bundle());
		this.name = key.getId();
	}
	
	public GenericResourceValueReference(ResourceProvider provider, String name) {
		if (provider == null) {
			throw new NullPointerException("resources");
		}
		if (name == null) {
			throw new NullPointerException("key");
		}
		this.provider = provider;
		this.name = name;
	}

	@Override
	public OptionalString fetch(ResourceResolutionContext context) {
		return provider.get(name, context);
	}

	@Override
	public OptionalString in(Object... components) {
		return fetch(ResourceResolutionContext.in(components));
	}

}
