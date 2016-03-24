package com.github.resource4j.objects.providers.resolvers;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class ResourceObjectProviderPredicates {

    private ResourceObjectProviderPredicates() {
    }

    public static Predicate<String> name(String pattern) {
        return Pattern.compile(pattern).asPredicate();
    };


}
