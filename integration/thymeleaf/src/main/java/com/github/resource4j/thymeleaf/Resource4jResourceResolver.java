package com.github.resource4j.thymeleaf;

import java.io.InputStream;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.IContext;
import org.thymeleaf.resourceresolver.IResourceResolver;

import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.resources.Resources;

public class Resource4jResourceResolver implements IResourceResolver {

	private static final String NAME = "RESOURCE4J-RESOURCE";

	private static final Logger LOG = LoggerFactory.getLogger(Resource4jResourceResolver.class);
	
	private Resources resources;

	public Resource4jResourceResolver(Resources resources) {
		this.resources = resources;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public InputStream getResourceAsStream(
			TemplateProcessingParameters templateProcessingParameters,
			String resourceName) {
		IContext context = templateProcessingParameters.getContext();
		Locale locale = context.getLocale();
		try {
			return resources.contentOf(resourceName, locale).asStream();
		} catch (MissingResourceObjectException e) {
			LOG.error("Resource {} not found.", resourceName, e);
			// According to contract for IResourceResolver
			return null;
		}
	}

}
