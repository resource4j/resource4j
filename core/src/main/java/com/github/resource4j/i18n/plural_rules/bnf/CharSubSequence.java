package com.github.resource4j.i18n.plural_rules.bnf;

public class CharSubSequence implements CharSequence {

    private String value;

    private int start;

    private int end;


    public CharSubSequence(String value, int start, int end) {
        this.value = value;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index + start);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharSubSequence(value, this.start + start, this.start + end);
    }

    @Override
    public String toString() {
        return value.substring(start, end);
    }
}
