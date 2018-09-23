package com.github.resource4j;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceValueReference {
	
	OptionalString in(Object... components);
	
	OptionalString fetch(ResourceResolutionContext context);
	
}
