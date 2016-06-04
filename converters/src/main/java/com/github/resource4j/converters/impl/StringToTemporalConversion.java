package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.*;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;

public class StringToTemporalConversion implements Conversion<String,Temporal> {
    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(String.class, Instant.class));
        conversionPairs.add(new ConversionPair(String.class, LocalDateTime.class));
        conversionPairs.add(new ConversionPair(String.class, LocalDate.class));
        conversionPairs.add(new ConversionPair(String.class, LocalTime.class));
        conversionPairs.add(new ConversionPair(String.class, ZonedDateTime.class));
        conversionPairs.add(new ConversionPair(String.class, HijrahDate.class));
        conversionPairs.add(new ConversionPair(String.class, JapaneseDate.class));
        conversionPairs.add(new ConversionPair(String.class, ThaiBuddhistDate.class));
        conversionPairs.add(new ConversionPair(String.class, MinguoDate.class));
        conversionPairs.add(new ConversionPair(String.class, Year.class));
        conversionPairs.add(new ConversionPair(String.class, YearMonth.class));
        return conversionPairs;
    }

    @Override
    public Temporal convert(String fromValue, Class<Temporal> toType, Optional<Object> pattern) throws TypeCastException {
        DateTimeFormatter formatter = null;
        if (pattern.isPresent()) {
            Object formatObject = pattern.get();
            if (formatObject instanceof String) {
                formatter = ofPattern((String) formatObject);
            }
        }
        if (formatter == null) {
            formatter = ChronoLocalDate.class.isAssignableFrom(toType)
                    ? DateTimeFormatter.ISO_DATE
                    : DateTimeFormatter.ISO_DATE_TIME;
        }
        TemporalAccessor accessor = formatter.parse(fromValue);
        try {
            Method method = toType.getMethod("from", TemporalAccessor.class);
            if (Temporal.class.isAssignableFrom(method.getReturnType())
                    && Modifier.isStatic(method.getModifiers())) {
                return (Temporal) method.invoke(null, accessor);
            }
            throw new TypeCastException(fromValue, fromValue.getClass(), toType, "unsupported target type");
        } catch (Exception e) {
            throw new TypeCastException(fromValue, fromValue.getClass(), toType, e);
        }
    }
}
