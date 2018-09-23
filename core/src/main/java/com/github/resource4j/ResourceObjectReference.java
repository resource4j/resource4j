package com.github.resource4j;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceObjectReference {

	String name();
	
	ResourceObject get(ResourceResolutionContext context);
	
}
