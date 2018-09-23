package com.github.resource4j.spring.context;

import com.github.resource4j.resources.context.ResourceResolutionContext;

import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;

public class EmptyResolutionContextProvider implements ResolutionContextProvider {

	@Override
	public ResourceResolutionContext getContext() {
		return withoutContext();
	}

}
