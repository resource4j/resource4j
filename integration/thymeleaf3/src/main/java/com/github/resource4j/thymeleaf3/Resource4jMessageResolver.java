package com.github.resource4j.thymeleaf3;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.util.Validate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.ResourceKey.plain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.context;
import static com.github.resource4j.resources.context.ResourceResolutionContext.resolve;

@SuppressWarnings("WeakerAccess")
public class Resource4jMessageResolver extends AbstractMessageResolver {

	private static final Logger LOG = LoggerFactory.getLogger(Resource4jMessageResolver.class);
	
	private Resources resources;

	private StandardMessageResolver standardMessageResolver = new StandardMessageResolver();
	
	public Resource4jMessageResolver(Resources resources) {
		this.resources = resources;
	}

    @Override
    @SuppressWarnings("unchecked")
    public String resolveMessage(ITemplateContext context, Class<?> origin,
                                 String key, Object[] messageParameters) {
        Validate.notNull(context, "Arguments cannot be null");
        Locale locale = context.getLocale();
        Validate.notNull(locale, "Locale in context cannot be null");
        Validate.notNull(key, "Message key cannot be null");
        String templateName = context.getTemplateData().getTemplate();
        LOG.trace("[THYMELEAF][{}] Resolving message with key \"{}\" for template \"{}\" and locale \"{}\". Messages will be retrieved from Spring's MessageSource infrastructure.", TemplateEngine.threadIndex(), key, templateName, locale);
        try {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < messageParameters.length; i++) {
                if (messageParameters[i] instanceof Map) {
                    // it's safe, because it's intended only for the read-only
                    // access by String key
                    params.putAll((Map) messageParameters[i]);
                } else {
                    params.put(String.valueOf(i), messageParameters[i]);
                }
            }
            ResourceResolutionContext resolutionContext = context(resolve(locale), params);
            return resources.get(plain(key), resolutionContext)
                    .notNull()
                    .asIs();
        } catch (MissingValueException e) {
            // According to contract for message resolver
            return null;
        }
    }

    @Override
    public String createAbsentMessageRepresentation(ITemplateContext context, Class<?> origin,
                                                    String key, Object[] messageParameters) {
        return this.standardMessageResolver.createAbsentMessageRepresentation(context, origin, key, messageParameters);
    }

}
