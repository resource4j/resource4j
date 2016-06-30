package com.github.resource4j.converters.impl;

import com.github.resource4j.converters.ConversionPair;
import com.github.resource4j.converters.TypeCastException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

public class StringToNumberConversion implements PrimitiveTypeConversion<String,Number> {

    Map<Class<?>,Function<Number,? extends Number>> NUMBER_CONVERSIONS = Collections.unmodifiableMap(of(
            entry(Number.class, Function.identity()),
            entry(Byte.class, convert(Number::byteValue)),
            entry(Short.class, convert(Number::shortValue)),
            entry(Integer.class, convert(Number::intValue)),
            entry(Long.class, convert(Number::longValue)),
            entry(BigInteger.class, convert(v -> BigInteger.valueOf(v.longValue()))),
            entry(Float.class, convert(Number::floatValue)),
            entry(Double.class, convert(Number::doubleValue)),
            entry(BigDecimal.class, convert(v -> new BigDecimal(v.doubleValue())))
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

    public static SimpleEntry<Class<?>, Function<Number,Number>> entry(
            Class<?> type,
            Function<Number,Number> function
    ) {
        return new SimpleEntry<>(type, function);
    }

    public static Function<Number,Number> convert(Function<Number,Number> fn) {
        return fn;
    }

    @Override
    public Set<ConversionPair> acceptedTypes() {
        Set<ConversionPair> conversionPairs = new HashSet<>();
        for (Map.Entry<Class<?>,Class<?>> entry : PRIMITIVE_TYPE_MAPPING.entrySet()) {
            if (Number.class.isAssignableFrom(entry.getValue())) {
                conversionPairs.add(new ConversionPair(String.class, entry.getKey()));
                conversionPairs.add(new ConversionPair(String.class, entry.getValue()));
            }
        }
        conversionPairs.add(new ConversionPair(String.class, Number.class));
        conversionPairs.add(new ConversionPair(String.class, BigDecimal.class));
        conversionPairs.add(new ConversionPair(String.class, BigInteger.class));
        return conversionPairs;
    }

    @Override
    public Number convert(String fromValue, Class<Number> toType, Optional<Object> pattern) throws TypeCastException {
        NumberFormat formatter = null;
        if (pattern.isPresent()) {
            Object formatObject = pattern.get();
            if (formatObject instanceof String) {
                DecimalFormat df = new DecimalFormat((String) formatObject);
                if (toType.equals(BigDecimal.class) || toType.equals(BigInteger.class)) {
                    df.setParseBigDecimal(true);
                }
            } else if (formatObject instanceof NumberFormat) {
                formatter = (NumberFormat) formatObject;
            }
        }
        if (formatter == null) {
            formatter = NumberFormat.getNumberInstance(Locale.US);
        }
        try {
            Number result = formatter.parse(fromValue); // Long or Double or BigDecimal
            if (!toType.isInstance(result)) {
                if (result instanceof BigDecimal && BigInteger.class.equals(toType)) {
                    result = ((BigDecimal) result).toBigInteger();
                } else {
                    Class<?> type = toType.isPrimitive() ? PRIMITIVE_TYPE_MAPPING.get(toType) : toType;
                    result = NUMBER_CONVERSIONS.get(type).apply(result);
                }
            }
            return result;
        } catch (ParseException e) {
            throw new TypeCastException(fromValue, fromValue.getClass(), toType, e);
        }
    }

}
