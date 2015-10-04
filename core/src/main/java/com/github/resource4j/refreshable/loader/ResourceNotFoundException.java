package com.github.resource4j.refreshable.loader;

import com.github.resource4j.ResourceException;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class ResourceNotFoundException extends ResourceException {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(Object key, ResourceResolutionContext ctx) {
		super(String.valueOf(key) + "[" + ctx + "]");
	}
	
}
