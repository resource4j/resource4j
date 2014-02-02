package com.github.resource4j.resources.references;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceFileReference {

	String name();
	
	ResourceFile get(ResourceResolutionContext context);
	
}
