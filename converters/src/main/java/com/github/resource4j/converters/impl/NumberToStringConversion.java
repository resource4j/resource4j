package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NumberToStringConversion implements PrimitiveTypeConversion<Number,String> {


    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        for (Map.Entry<Class<?>,Class<?>> entry : PRIMITIVE_TYPE_MAPPING.entrySet()) {
            if (Number.class.isAssignableFrom(entry.getKey())) {
                conversionPairs.add(new ConversionPair(entry.getKey(), String.class));
                conversionPairs.add(new ConversionPair(entry.getValue(), String.class));
            }
        }
        return conversionPairs;
    }

    @Override
    public String convert(Number fromValue, Class<String> toType, Object format) throws TypeCastException {
        NumberFormat formatter = null;
        if (format != null) {
            if (format instanceof String) {
                formatter = new DecimalFormat((String) format);
            } else if (format instanceof NumberFormat) {
                formatter = (NumberFormat) format;
            }
        }
        if (formatter == null) {
            formatter = NumberFormat.getInstance();
        }
        return formatter.format(fromValue);
    }

}
