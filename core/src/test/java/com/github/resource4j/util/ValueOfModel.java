package com.github.resource4j.util;

public class ValueOfModel {

    private String value1;

    private String value2;

    public ValueOfModel(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    @Override
    public String toString() {
        return value1 + "," + value2;
    }

    public static ValueOfModel valueOf(String s) {
        String[] vals = s.split(",");
        if (vals.length != 2) {
            throw new IllegalArgumentException(s);
        }
        return new ValueOfModel(vals[0], vals[1]);
    }

}
