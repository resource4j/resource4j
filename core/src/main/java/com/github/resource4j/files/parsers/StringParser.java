package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.io.InputStream;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.generic.GenericOptionalString;

/**
 * @author Ivan Gammel
 */
public class StringParser extends AbstractParser<String, OptionalString> {

    /**
     * TODO: replace with StandardCharsets.UTF-8 some time.
     */
    public static final String STANDARD_CHARSET_UTF_8 = "UTF-8";
    
    private static final StringParser INSTANCE = new StringParser(STANDARD_CHARSET_UTF_8);

    public static StringParser getInstance() {
        return INSTANCE;
    }
    
    private String charsetName;

    public StringParser(String charsetName) {
        super();
        if (charsetName == null) {
            throw new IllegalArgumentException("charsetName cannot be null");
        }
        this.charsetName = charsetName;
    }

    @Override
    protected OptionalString createValue(ResourceFile file, ResourceKey key, String value, Throwable suppressedException) {
        return new GenericOptionalString(file.resolvedName(), key, value, suppressedException);
    }

    @Override
    public String parse(InputStream stream) throws IOException {
        // Here's the nice solution from StackOverflow: http://stackoverflow.com/a/5445161
    	try (java.util.Scanner s = new java.util.Scanner(stream, charsetName)) {
    		s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
    	}
    }
}
