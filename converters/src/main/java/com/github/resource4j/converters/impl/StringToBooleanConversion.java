package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

public class StringToBooleanConversion implements Conversion<String,Boolean> {

    private final static Set<String> TRUE_VALUES = unmodifiableSet(of("true","on","1","enabled","checked").collect(toSet()));

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return Stream.of(
                new ConversionPair(String.class, Boolean.class),
                new ConversionPair(String.class, Boolean.TYPE)
        ).collect(Collectors.toSet());
    }

    @Override
    public Boolean convert(String fromValue, Class<Boolean> toType, Optional<Object> pattern) throws TypeCastException {
        return TRUE_VALUES.contains(fromValue.toLowerCase());
    }

}
