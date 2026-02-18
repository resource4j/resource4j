package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.util.function.Function;


public class StringCaseStrategy implements PropertyResolver {

    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        if (value instanceof String string) {
            return switch (property) {
                case "upper" -> string.toUpperCase();
                case "lower" -> string.toLowerCase();
                case "upperFirst" -> first(string, Character::toUpperCase);
                case "lowerFirst" -> first(string, Character::toLowerCase);
                default -> null;
            };
        }
        return null;
    }

    private String first(String string, Function<Character,Character> function) {
        if (string.length() < 1) return string;
        return function.apply(string.charAt(0)) + string.substring(1);
    }

}
