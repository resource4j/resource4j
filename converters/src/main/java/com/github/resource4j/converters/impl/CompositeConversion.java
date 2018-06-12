package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.*;

@SuppressWarnings({"unchecked", "raw"})
public class CompositeConversion implements Conversion<Object, Object> {

    private final Conversion first;
    private final Conversion second;

    private final ConversionPair firstPair;
    private final ConversionPair secondPair;


    public CompositeConversion(ConversionPair firstPair, ConversionPair secondPair, Conversion<?, ?> first, Conversion<?, ?> second) {
        this.first = first;
        this.second = second;

        this.firstPair = firstPair;
        this.secondPair = secondPair;
    }

    @Override
    public Set<ConversionPair> acceptedTypes() {
        ConversionPair pair = new ConversionPair(firstPair.from(), secondPair.to());
        return Collections.singleton(pair);
    }

    @Override
    public Object convert(Object fromValue, Class<Object> toType, Object format) throws TypeCastException {
        Object val = first.convert(fromValue, firstPair.to(), format);
        return second.convert(val, secondPair.to(), format);
    }

    public String toString() {
        return firstPair.from().getSimpleName() + " -> " + firstPair.to().getSimpleName() + " -> " + secondPair.to().getSimpleName();
    }

}
