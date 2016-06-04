package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

public interface PrimitiveTypeConversion<F,T> extends Conversion<F,T> {

    Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAPPING = Collections.unmodifiableMap(of(
            new SimpleEntry<>(Boolean.TYPE, Boolean.class),
            new SimpleEntry<>(Character.TYPE, Character.class),
            new SimpleEntry<>(Byte.TYPE, Byte.class),
            new SimpleEntry<>(Short.TYPE, Short.class),
            new SimpleEntry<>(Integer.TYPE, Integer.class),
            new SimpleEntry<>(Long.TYPE, Long.class),
            new SimpleEntry<>(Float.TYPE, Float.class),
            new SimpleEntry<>(Double.TYPE, Double.class)
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

}
