package com.github.resource4j.resources.references;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class GenericResourceFileReference implements ResourceFileReference {

	private Resources resources;
	
	private String name;
	
	public GenericResourceFileReference(Resources resources, String name) {
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
	public ResourceFile get(ResourceResolutionContext context) {
		return resources.contentOf(name, context);
	}

}
