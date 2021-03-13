package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.IMessageResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.ResourceKey.plain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.*;

public class Resource4jMessageResolver extends AbstractMessageResolver {

    private final Resources resources;

    public Resource4jMessageResolver(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String resolveMessage(ITemplateContext context,
                                 Class<?> origin,
                                 String key,
                                 Object[] messageParameters) {
        Locale locale = context.getLocale();
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
        ResourceResolutionContext resolutionContext = locale != null
                ? context(resolve(locale), params)
                : withParams(params);
        return resources.get(plain(key), resolutionContext).asIs();
    }

    @Override
    public String createAbsentMessageRepresentation(ITemplateContext context,
                                                    Class<?> origin,
                                                    String key,
                                                    Object[] messageParameters) {
        if (context.getLocale() != null) {
            return "??"+key+"_" + context.getLocale().toString() + "??";
        }
        return "??"+key+"_" + "??";
    }
}
