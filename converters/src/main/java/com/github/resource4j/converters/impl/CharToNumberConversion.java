package com.github.resource4j.converters.impl;


import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CharToNumberConversion implements PrimitiveTypeConversion<Character,Number> {

    private String numbers = "0123456789ABCDEF";

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        for (Map.Entry<Class<?>,Class<?>> entry : PRIMITIVE_TYPE_MAPPING.entrySet()) {
            if (Number.class.isAssignableFrom(entry.getKey())) {
                conversionPairs.add(new ConversionPair(Character.class, entry.getKey()));
                conversionPairs.add(new ConversionPair(Character.class, entry.getValue()));
                conversionPairs.add(new ConversionPair(Character.TYPE, entry.getKey()));
                conversionPairs.add(new ConversionPair(Character.TYPE, entry.getValue()));
            }
        }
        return conversionPairs;
    }

    @Override
    public Number convert(Character fromValue, Class<Number> toType, Optional<Object> format) throws TypeCastException {
        int value = numbers.indexOf(fromValue);
        if (value >= 0) {
            return value;
        }
        throw new TypeCastException(fromValue, fromValue.getClass(), toType, "character is not hex digit");
    }
}
