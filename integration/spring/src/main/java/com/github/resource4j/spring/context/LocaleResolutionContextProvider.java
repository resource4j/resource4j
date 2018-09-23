package com.github.resource4j.spring.context;

import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.Locale;

public class LocaleResolutionContextProvider implements ResolutionContextProvider {

	@Override
	public ResourceResolutionContext getContext() {
		return ResourceResolutionContext.in(Locale.getDefault());
	}

}
