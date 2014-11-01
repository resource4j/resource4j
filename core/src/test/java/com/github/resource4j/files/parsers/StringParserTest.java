package com.github.resource4j.files.parsers;

import static com.github.resource4j.ResourceKey.bundle;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.resource4j.files.ByteArrayResourceFile;
import com.github.resource4j.files.ResourceFile;

public class StringParserTest {

    @Test
    public void testOneLineStringParsing() throws IOException {
        String line = "Lorem Ipsum Dolorem";
        ResourceFile file = new ByteArrayResourceFile(bundle("test.txt"), line.getBytes());
        String result = StringParser.getInstance().parse(file);
        assertEquals(line, result);
    }

    @Test
    public void testMultiLineStringParsing() throws IOException {
        String line = "Lorem\r\nIpsum\n\tDolorem";
        ResourceFile file = new ByteArrayResourceFile(bundle("test.txt"), line.getBytes());
        String result = StringParser.getInstance().parse(file);
        assertEquals(line, result);
    }
}
