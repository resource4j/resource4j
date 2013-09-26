package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.io.InputStream;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.generic.GenericOptionalString;

/**
 * @author Ivan Gammel
 */
public class StringParser extends AbstractParser<String, OptionalString> {

    private static final StringParser INSTANCE = new StringParser();

    public static StringParser getInstance() {
        return INSTANCE;
    }

    @Override
    protected OptionalString createValue(ResourceKey key, String value, Throwable suppressedException) {
        return new GenericOptionalString(key, value, suppressedException);
    }

    @Override
    public String parse(InputStream stream) throws IOException {
        // Here's the nice solution from StackOverflow: http://stackoverflow.com/a/5445161
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
