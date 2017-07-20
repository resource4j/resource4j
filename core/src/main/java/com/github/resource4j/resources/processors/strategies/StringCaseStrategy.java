package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.util.function.Function;


public class StringCaseStrategy implements PropertyResolver {

    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        if (value != null && value instanceof String) {
            String string = (String) value;
            switch (property) {
                case "upper": return string.toUpperCase();
                case "lower": return string.toLowerCase();
                case "upperFirst": return first(string, Character::toUpperCase);
                case "lowerFirst": return first(string, Character::toUpperCase);
            }
        }
        return null;
    }

    private String first(String string, Function<Character,Character> function) {
        if (string.length() < 1) return string;
        return function.apply(string.charAt(0)) + string.substring(1);
    }

}
