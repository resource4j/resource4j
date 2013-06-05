package com.github.resource4j.files;

import java.io.InputStream;

public class StringParser implements ResourceParser<String> {

    private static final StringParser INSTANCE = new StringParser();

    @Override
    public String parse(InputStream stream) {
        return null;
    }

    public static StringParser getInstance() {
        return INSTANCE;
    }

}
