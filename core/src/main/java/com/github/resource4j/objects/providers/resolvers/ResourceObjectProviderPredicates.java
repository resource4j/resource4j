package com.github.resource4j.objects.providers.resolvers;

import com.github.resource4j.resources.context.LocaleResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class ResourceObjectProviderPredicates {

    private ResourceObjectProviderPredicates() {
    }

    public static Predicate<String> name(String pattern) {
        return Pattern.compile(pattern).asPredicate();
    }

    public static Predicate<String> extension(String ext) {
        return name -> name.endsWith("." + ext);
    }

    public static Predicate<ResourceResolutionContext> empty() {
        return ResourceResolutionContext::isEmpty;
    }

    public static Predicate<ResourceResolutionContext> contains(ResourceResolutionContext ctx) {
        return c -> c.contains(ctx);
    }

    public static Predicate<ResourceResolutionContext> i18n() {
        return c -> {
            for (ResourceResolutionComponent component : c.components()) {
                if (component instanceof LocaleResolutionComponent) {
                    return true;
                }
            }
            return false;
        };
    }

}
