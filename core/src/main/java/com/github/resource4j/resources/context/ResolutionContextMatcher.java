package com.github.resource4j.resources.context;

import java.util.List;

public interface ResolutionContextMatcher {

	List<ResourceResolutionContext> matches(ResourceResolutionContext context);
	
}
