package com.github.resource4j.converters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ConversionPair {

    Class<?> from;

    Class<?> to;

    public ConversionPair(Class<?> from, Class<?> to) {
        this.from = from;
        this.to = to;
    }

    public Class<?> from() {
        return from;
    }

    public Class<?> to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionPair that = (ConversionPair) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public static ConversionPair of(Class<?> from, Class<?> to) {
        return new ConversionPair(from, to);
    }

    public String toString() {
        return from.getName() + " / " + to.getName();
    }

    public static Set<ConversionPair> pairs(ConversionPair... pairs) {
        return new HashSet<>(Arrays.asList(pairs));
    }
}
