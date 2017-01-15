package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;


public class SQLTimeToLocalTimeConversion implements Conversion<Time, LocalTime> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        return Collections.singleton(new ConversionPair(Time.class, LocalTime.class));
    }

    @Override
    public LocalTime convert(Time fromValue, Class<LocalTime> toType, Object format) throws TypeCastException {
        return fromValue.toLocalTime();
    }
}
