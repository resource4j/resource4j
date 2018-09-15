package com.github.resource4j.thymeleaf3;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.ResourceKey.plain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.context;
import static com.github.resource4j.resources.context.ResourceResolutionContext.resolve;

public class Resource4jMessageSource implements MessageSource {

    private static final Logger LOG = LoggerFactory.getLogger(Resource4jMessageSource.class);

    private Resources resources;

    public Resource4jMessageSource(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
            return getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            return defaultMessage;
        }
    }

    @Override
    public String getMessage(String key, Object[] messageParameters, Locale locale) throws NoSuchMessageException {
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
            throw new NoSuchMessageException(key, locale);
        }
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        String[] codes = resolvable.getCodes();
        if (codes == null || codes.length == 0) {
            throw new IllegalArgumentException("At least one message code must be provided");
        }
        for (String code : codes) {
            try {
                return getMessage(code, resolvable.getArguments(), locale);
            } catch (NoSuchMessageException e) {
                LOG.trace("Message not found: {} in {}", code, locale);
            }
        }
        if (resolvable.getDefaultMessage() == null) {
            throw new NoSuchMessageException(codes[codes.length - 1], locale);
        }
        return resolvable.getDefaultMessage();
    }
}
