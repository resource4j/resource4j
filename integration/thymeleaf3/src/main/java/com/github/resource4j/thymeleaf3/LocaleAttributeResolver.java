package com.github.resource4j.thymeleaf3;

import org.thymeleaf.context.IContext;

public class LocaleAttributeResolver implements ContextSpecificAttributeResolver {

    private static final String LOCALE_ATTRIBUTE = "locale";

    @Override
    public String name() {
        return LOCALE_ATTRIBUTE;
    }

    @Override
    public Object resolve(IContext value) {
        return value.getLocale();
    }

}
