package com.github.resource4j.spring.context;

import java.util.Locale;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class LocaleResolutionContextProvider implements ResolutionContextProvider {

	@Override
	public ResourceResolutionContext getContext() {
		return ResourceResolutionContext.in(Locale.getDefault());
	}

}
