package com.github.resource4j.spring.context;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.withoutContext;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class EmptyResolutionContextProvider implements ResolutionContextProvider {

	@Override
	public ResourceResolutionContext getContext() {
		return withoutContext();
	}

}
