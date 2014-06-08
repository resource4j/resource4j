package com.github.resource4j.files.parsers;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class StringParserTest {

    @Test
    public void testOneLineStringParsing() throws IOException {
        String line = "Lorem Ipsum Dolorem";
        ByteArrayInputStream stream = new ByteArrayInputStream(line.getBytes());
        String result = StringParser.getInstance().parse(stream);
        assertEquals(line, result);
    }

    @Test
    public void testMultiLineStringParsing() throws IOException {
        String line = "Lorem\r\nIpsum\n\tDolorem";
        ByteArrayInputStream stream = new ByteArrayInputStream(line.getBytes());
        String result = StringParser.getInstance().parse(stream);
        assertEquals(line, result);
    }
}
