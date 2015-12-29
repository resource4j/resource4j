package com.github.resource4j.files.parsers;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.github.resource4j.ResourceObject;
import org.junit.Test;

import com.github.resource4j.generic.objects.ByteArrayResourceObject;

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
