package com.github.resource4j.thymeleaf;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.MessageResolution;
import org.thymeleaf.util.Validate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.ResourceKey.plain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.context;
import static com.github.resource4j.resources.context.ResourceResolutionContext.resolve;

public class Resource4jMessageResolver extends AbstractMessageResolver {

	private static final Logger LOG = LoggerFactory.getLogger(Resource4jMessageResolver.class);
	
	private Resources resources;
	
	public Resource4jMessageResolver(Resources resources) {
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
		LOG.trace("[THYMELEAF][{}] Resolving message with key \"{}\" for template \"{}\" and locale \"{}\". Messages will be retrieved from Spring's MessageSource infrastructure.", TemplateEngine.threadIndex(), key, templateName, locale);
        try {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < messageParameters.length; i++) {
                if (messageParameters[i] instanceof Map) {
                    ((Map<?, ?>) messageParameters[i]).forEach((k, v) -> {
                        params.put(String.valueOf(k), v);
                    });
                } else {
                    params.put(String.valueOf(i), messageParameters[i]);
                }
            }
            ResourceResolutionContext context = context(resolve(locale), params);
            String resolvedMessage = resources.get(plain(key), context)
            		.notNull()
            		.asIs();
            // TODO: why was it here?
//            resolvedMessage = resolvedMessage.replace("'", "\\'");
            return new MessageResolution(resolvedMessage);
        } catch (MissingValueException e) {
        	// According to contract for message resolver
            return null;
        }
	}
	
}
