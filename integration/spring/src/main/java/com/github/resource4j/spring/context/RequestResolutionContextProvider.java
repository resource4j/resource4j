package com.github.resource4j.spring.context;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;

import org.springframework.context.i18n.LocaleContextHolder;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class RequestResolutionContextProvider implements ResolutionContextProvider {

	private static final String WEB_CONTEXT = "WEB_CONTEXT";
	
	private String webContextName = WEB_CONTEXT;
	
	public RequestResolutionContextProvider() {
	}
	
	public RequestResolutionContextProvider(String webContextName) {
		super();
		this.webContextName = webContextName;
	}

	public String getWebContextName() {
		return webContextName;
	}

	public void setWebContextName(String webContextName) {
		this.webContextName = webContextName;
	}

	@Override
	public ResourceResolutionContext getContext() {
		return in(LocaleContextHolder.getLocale(), webContextName);
	}

}
