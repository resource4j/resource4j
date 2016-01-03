package com.github.resource4j.test;

import java.util.Random;

public class Builders {

    private static final String DIGITS = "0123456789";
    private static final String LATIN_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String IDENTIFIER_CHARS = LATIN_LETTERS + DIGITS + "_$";

    public static <T> T given(Builder<T> builder) {
        return builder.build();
    }

    private static char nextChar(Random random, String charset) {
        int index = random.nextInt(charset.length());
        return charset.charAt(index);
    }

    public static String anyIdentifier() {
        Random random = new Random();
        int length =  random.nextInt(5) + 3;
        StringBuilder builder = new StringBuilder();
        builder.append(nextChar(random, LATIN_LETTERS));
        for (int i = 0; i < length; i++) {
            builder.append(nextChar(random, IDENTIFIER_CHARS));
        }
        return builder.toString();
    }

}
