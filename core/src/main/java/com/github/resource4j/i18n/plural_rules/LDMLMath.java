package com.github.resource4j.i18n.plural_rules;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class LDMLMath {

    private static Map<Class<? extends Number>, Function<Number, Number>> ABS =
            add(Integer.class, Math::abs)
            .add(Long.class, Math::abs)
            .add(Byte.class, val -> Math.abs(val.intValue()))
            .add(Short.class, val -> Math.abs(val.intValue()))
            .add(Float.class, Math::abs)
            .add(Double.class, Math::abs)
            .add(BigInteger.class, BigInteger::abs)
            .add(BigDecimal.class, BigDecimal::abs)
            .build();

    public static Number abs(Number number) {
        return ABS.get(number.getClass()).apply(number);
    }

    public static Number trunc(Number number) {
        if (number instanceof Float || number instanceof Double) {
           return (long) number.doubleValue();
        } else if (number instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) number;
            return decimal.divideToIntegralValue(BigDecimal.valueOf(1));
        } else {
            return number;
        }
    }

    public static Optional<BigInteger> fraction(Number number, boolean preserveTrailingZeros) {
        if (number instanceof Float || number instanceof Double) {
            number = BigDecimal.valueOf(number.doubleValue());
        }
        if (number instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) number;
            decimal = (preserveTrailingZeros ? decimal : decimal.stripTrailingZeros());
            return Optional.of(decimal
                    .remainder(BigDecimal.ONE)
                    .movePointRight(decimal.scale())
                    .abs()
                    .toBigInteger());
        } else {
            return Optional.empty();
        }
    }


    public static Number size(BigInteger fraction) {
        return fraction == null ? 0 : fraction.toString(10).length();
    }

    private static <T extends Number> Builder add(Class<T> type, Function<T, Number> function) {
        return new Builder().add(type, function);
    }

    private static class Builder {
        private Map<Class<? extends Number>, Function<Number, Number>> map = new HashMap<>();
        @SuppressWarnings({"raw", "unchecked"})
        private <T extends Number> Builder add(Class<T> type, Function<T, Number> function) {
            map.put(type, (Function) function);
            return this;
        }
        private Map<Class<? extends Number>, Function<Number, Number>> build() {
            return Collections.unmodifiableMap(map);
        }
    }

}
