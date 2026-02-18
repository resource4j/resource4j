package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.lang.reflect.Method;

/**
 * Resolves a property on a value object using JavaBean getter conventions.
 * For a property "name", tries in order: getName(), isName(), name().
 * This is the last strategy in the chain, acting as a catch-all for object property access.
 */
public class ReflectionStrategy implements PropertyResolver {

    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        if (value == null || property == null || property.isEmpty()) {
            return null;
        }
        String capitalized = Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Method method = findMethod(value.getClass(), "get" + capitalized);
        if (method == null) {
            method = findMethod(value.getClass(), "is" + capitalized);
        }
        if (method == null) {
            method = findMethod(value.getClass(), property);
        }
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method findMethod(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
