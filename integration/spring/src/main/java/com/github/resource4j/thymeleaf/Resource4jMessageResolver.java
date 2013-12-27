package com.github.resource4j.thymeleaf;

import static com.github.resource4j.ResourceKey.plain;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.MessageResolution;
import org.thymeleaf.util.Validate;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.resources.Resources;

public class Resource4jMessageResolver extends AbstractMessageResolver {

	private static final Logger LOG = LoggerFactory.getLogger(Resource4jMessageResolver.class);
	
	private Resources resources;
	
	public void setResources(Resources resources) {
		this.resources = resources;
	}

	@Override
	public MessageResolution resolveMessage(Arguments arguments, String key,
			Object[] messageParameters) {
		Validate.notNull(arguments, "Arguments cannot be null");
        Locale locale = arguments.getContext().getLocale();
		Validate.notNull(locale, "Locale in context cannot be null");
        Validate.notNull(key, "Message key cannot be null");

        String templateName = arguments.getTemplateName();
		LOG.trace("[THYMELEAF][{}] Resolving message with key \"{}\" for template \"{}\" and locale \"{}\". Messages will be retrieved from Spring's MessageSource infrastructure.", new Object[] {TemplateEngine.threadIndex(), key, templateName, locale});
        try {
            final String resolvedMessage = resources.get(plain(key), locale)
            		.notNull()
            		.asFormatted(messageParameters);
            return new MessageResolution(resolvedMessage);
        } catch (MissingValueException e) {
        	// According to contract for message resolver
            return null;
        }
	}
	
}
