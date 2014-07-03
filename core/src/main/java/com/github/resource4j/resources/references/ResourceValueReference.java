package com.github.resource4j.resources.references;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceValueReference {

	ResourceKey key();
	
	OptionalString in(Object... components);
	
	OptionalString fetch(ResourceResolutionContext context);
	
}
