package com.github.resource4j.objects.parsers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.ByteArrayResourceObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringParserTest {

    @Test
    public void testOneLineStringParsing() throws IOException {
        String line = "Lorem Ipsum Dolorem";
        ResourceObject file = new ByteArrayResourceObject("test.txt", "test.txt", line.getBytes(), 0);
        String result = StringParser.getInstance().parse(file);
        assertEquals(line, result);
    }

    @Test
    public void testMultiLineStringParsing() throws IOException {
        String line = "Lorem\r\nIpsum\n\tDolorem";
        ResourceObject file = new ByteArrayResourceObject("test.txt", "test.txt", line.getBytes(), 0);
        String result = StringParser.getInstance().parse(file);
        assertEquals(line, result);
    }
}
