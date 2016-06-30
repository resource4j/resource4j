package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.*;

public class DateToCalendarConversion implements Conversion<java.util.Date,Calendar> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(java.util.Date.class, Calendar.class));
        conversionPairs.add(new ConversionPair(java.sql.Date.class, Calendar.class));
        conversionPairs.add(new ConversionPair(java.util.Date.class, GregorianCalendar.class));
        conversionPairs.add(new ConversionPair(java.sql.Date.class, GregorianCalendar.class));
        return conversionPairs;
    }

    @Override
    public Calendar convert(Date fromValue, Class<Calendar> toType, Optional<Object> pattern) throws TypeCastException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(fromValue);
        return calendar;
    }
}
