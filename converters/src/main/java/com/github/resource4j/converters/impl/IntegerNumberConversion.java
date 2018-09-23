package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class IntegerNumberConversion<T1 extends Number, T2 extends Number> implements Conversion<T1, T2> {

    private static final List<Class<? extends Number>> NUMBERS = unmodifiableList(asList(Byte.class, Short.class, Integer.class, Long.class));

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> result = new HashSet<>();
        for (Class<? extends Number> type1 : NUMBERS) {
            for (Class<? extends Number> type2 : NUMBERS) {
                if (type1 == type2) continue;
                result.add(new ConversionPair(type1, type2));
                result.add(new ConversionPair(primitiveOf(type1), type2));
                result.add(new ConversionPair(type1, primitiveOf(type2)));
                result.add(new ConversionPair(primitiveOf(type1), primitiveOf(type2)));
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private Class<?> primitiveOf(Class<? extends Number> type) {
        try {
            return (Class<?>) type.getField("TYPE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("No accessible TYPE field in number class " + type.getName(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T2 convert(T1 fromValue, Class<T2> toType, Object format) throws TypeCastException {
        if (Byte.class == toType || Byte.TYPE == toType) {
            return (T2) (Byte) fromValue.byteValue();
        } else if (Short.class == toType || Short.TYPE == toType) {
            return (T2) (Short) fromValue.shortValue();
        } else if (Integer.class == toType || Integer.TYPE == toType) {
            return (T2) (Integer) fromValue.intValue();
        } else {
            return (T2) (Long) fromValue.longValue();
        }
    }
}
