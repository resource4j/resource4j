package com.github.resource4j.i18n.plural_rules.ldml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

class NumberComparator implements Comparator<Number> {

    static NumberComparator compareNumbers() {
        return new NumberComparator();
    }

    @SuppressWarnings({ "unchecked", "raw" })
    public int compare(Number x, Number y) {
        if (x.getClass().equals(y.getClass()) && Comparable.class.isInstance(x)) {
            // all number types are comparable
            return Comparator.<Comparable>naturalOrder()
                    .compare((Comparable) x, (Comparable) y);
        }
        if(isSpecial(x) || isSpecial(y))
            return Double.compare(x.doubleValue(), y.doubleValue());
        else
            return toBigDecimal(x).compareTo(toBigDecimal(y));
    }

    private static boolean isSpecial(Number num) {
        boolean specialDouble = num instanceof Double
                && (Double.isNaN((Double) num) || Double.isInfinite((Double) num));
        boolean specialFloat = num instanceof Float
                && (Float.isNaN((Float) num) || Float.isInfinite((Float) num));
        return specialDouble || specialFloat;
    }

    private static BigDecimal toBigDecimal(Number number) {
        if(number instanceof BigDecimal)
            return (BigDecimal) number;
        if(number instanceof BigInteger)
            return new BigDecimal((BigInteger) number);
        if(number instanceof Byte || number instanceof Short
                || number instanceof Integer || number instanceof Long)
            return new BigDecimal(number.longValue());
        if(number instanceof Float || number instanceof Double)
            return new BigDecimal(number.doubleValue());

        try {
            return new BigDecimal(number.toString());
        } catch(final NumberFormatException e) {
            throw new RuntimeException("The given number (\"" + number + "\" of class " + number.getClass().getName() + ") does not have a parsable string representation", e);
        }
    }
}
