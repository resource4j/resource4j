package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Supports only java.util.Date and java.sql.Date
 * @param <E>
 */
public class LocalDateToDateConversion<E extends Date> implements Conversion<LocalDate, E> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(LocalDate.class, Date.class));
        conversionPairs.add(new ConversionPair(LocalDate.class, java.sql.Date.class));
        return conversionPairs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E convert(LocalDate fromValue, Class<E> toType, Object format) throws TypeCastException {
        if (java.sql.Date.class.equals(toType)) {
            return (E) java.sql.Date.valueOf(fromValue);
        } else try {
            ZoneId zoneId = TimeConversions.toZoneId(format);
            long timestamp = fromValue.atStartOfDay(zoneId).toInstant().toEpochMilli();
            return (E) new java.util.Date(timestamp);
        } catch (Exception e) {
            throw new TypeCastException(fromValue, LocalDate.class, toType, e);
        }

    }


}
