package com.github.resource4j.converters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public record ConversionPair(Class<?> from, Class<?> to) {

    public static ConversionPair of(Class<?> from, Class<?> to) {
        return new ConversionPair(from, to);
    }

    @Override
    public String toString() {
        return from.getName() + " / " + to.getName();
    }

    public static Set<ConversionPair> pairs(ConversionPair... pairs) {
        return new HashSet<>(Arrays.asList(pairs));
    }
}
