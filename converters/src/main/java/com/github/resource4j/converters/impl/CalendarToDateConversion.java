package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.Conversion;
import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.util.*;

public class CalendarToDateConversion implements Conversion<Calendar,Date> {

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        conversionPairs.add(new ConversionPair(Calendar.class, java.util.Date.class));
        conversionPairs.add(new ConversionPair(GregorianCalendar.class, java.util.Date.class));
        return conversionPairs;
    }

    @Override
    public Date convert(Calendar fromValue, Class<Date> toType, Object pattern) throws TypeCastException {
        return fromValue.getTime();
    }

}
