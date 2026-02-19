package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;


public class StringCaseStrategy implements PropertyResolver {

    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        Locale locale = context.locale();
        if (locale == null) locale = Locale.ROOT;
        if (value instanceof String string) {
            return switch (property) {
                case "upper" -> string.toUpperCase(locale);
                case "lower" -> string.toLowerCase(locale);
                case "upperFirst" -> first(string, locale, String::toUpperCase);
                case "lowerFirst" -> first(string, locale, String::toLowerCase);
                default -> null;
            };
        }
        return null;
    }

    private String first(String string, Locale locale, BiFunction<String, Locale, String> function) {
        if (string == null || string.isEmpty()) return string;
        int cp = string.codePointAt(0);
        int charCount = Character.charCount(cp);
        String first = new String(Character.toChars(cp));
        return function.apply(first, locale) + string.substring(charCount);
    }

}
