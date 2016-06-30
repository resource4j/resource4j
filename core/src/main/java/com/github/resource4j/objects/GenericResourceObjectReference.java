package com.github.resource4j.objects;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectReference;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public class GenericResourceObjectReference implements ResourceObjectReference {

	private Resources resources;
	
	private String name;
	
	public GenericResourceObjectReference(Resources resources, String name) {
		if (resources == null) {
			throw new NullPointerException("resources");
		}
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.resources = resources;
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ResourceObject get(ResourceResolutionContext context) {
		return resources.contentOf(name, context);
	}

}
