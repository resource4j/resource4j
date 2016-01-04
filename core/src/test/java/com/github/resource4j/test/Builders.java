package com.github.resource4j.test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Builders {

    private static final String DIGITS = "0123456789";
    private static final String LATIN_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String CYRILLIC_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя";
    private static final String IDENTIFIER_CHARS = LATIN_LETTERS + DIGITS + "_$";

    private static final String[] LETTERS = { LATIN_LETTERS, CYRILLIC_LETTERS };

    public static <T> T given(Builder<T> builder) {
        return builder.build();
    }

    private static char nextChar(Random random, String charset) {
        int index = random.nextInt(charset.length());
        return charset.charAt(index);
    }

    public static String anyIdentifier() {
        Random random = ThreadLocalRandom.current();
        int length =  random.nextInt(5) + 3;
        StringBuilder builder = new StringBuilder();
        builder.append(nextChar(random, LATIN_LETTERS));
        for (int i = 0; i < length; i++) {
            builder.append(nextChar(random, IDENTIFIER_CHARS));
        }
        return builder.toString();
    }

    public static String anyString() {
        Random random = ThreadLocalRandom.current();
        int words = random.nextInt(15) + 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words; i++) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            String charset = LETTERS[i % 2];
            int chars = random.nextInt(8) + 2;
            for (int j = 0; j < chars; j++) {
                int k = random.nextInt(charset.length());
                builder.append(charset.charAt(k));
            }
        }
        return builder.toString();
    }

    public static String anyText() {
        Random random = ThreadLocalRandom.current();
        int lines = random.nextInt(15) + 2;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            builder.append(anyString()).append("\r\n");
        }
        return builder.toString();
    }

}
