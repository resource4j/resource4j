package com.github.resource4j.util;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public final class IO {

    public final static int MAX_SIZE = 100*1024*1024;

    private IO() {
    }

    public static byte[] read(InputStream is) throws IOException {
        byte[] result = new byte[512];
        int currentSize = 0;
        while (currentSize < MAX_SIZE) {
            int bytesToRead;
            if (currentSize >= result.length) {
                bytesToRead = Math.min(MAX_SIZE - currentSize, result.length + 256);
                if (result.length < currentSize + bytesToRead) {
                    result = Arrays.copyOf(result, currentSize + bytesToRead);
                }
            } else {
                bytesToRead = result.length - currentSize;
            }
            int read = is.read(result, currentSize, bytesToRead);
            if (read < 0) {
                if (result.length != currentSize) {
                    result = Arrays.copyOf(result, currentSize);
                }
                break;
            }
            currentSize += read;
        }
        if (currentSize > MAX_SIZE - 1) {
            throw new ObjectTooBigException(MAX_SIZE - 1);
        }
        return result;
    }

    public static long size(InputStream is) throws IOException {
        byte[] result = new byte[4096];
        int currentSize = 0;
        while (currentSize < MAX_SIZE) {
            int bytesToRead = Math.min(MAX_SIZE - currentSize, 4096);
            int read = is.read(result, 0, bytesToRead);
            if (read < 0) {
                break;
            }
            currentSize += read;
        }
        return currentSize;
    }

}
