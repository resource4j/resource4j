package com.github.resource4j.resources.references;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceObjectReference {

	String name();
	
	ResourceObject get(ResourceResolutionContext context);
	
}
