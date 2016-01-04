package com.github.resource4j.objects;

import com.github.resource4j.test.Builder;
import com.github.resource4j.test.Builders;

public class ByteArrayResourceObjectBuilder implements Builder<ByteArrayResourceObject> {

    private String name = Builders.anyIdentifier();

    private String resolution = "-en_US";

    private String extension = ".txt";

    private byte[] content = Builders.anyText().getBytes();

    private long timestamp = System.currentTimeMillis();

    public static ByteArrayResourceObjectBuilder anObject() {
        return new ByteArrayResourceObjectBuilder();
    }

    public static ByteArrayResourceObjectBuilder anObject(String name, String resolution, String extension) {
        ByteArrayResourceObjectBuilder builder = new ByteArrayResourceObjectBuilder();
        builder.name = name;
        builder.extension = extension;
        builder.resolution = resolution;
        return builder;
    }

    public ByteArrayResourceObjectBuilder withContent(byte[] content) {
        this.content = content;
        return this;
    }

    public ByteArrayResourceObjectBuilder lastModifiedOn(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public ByteArrayResourceObject build() {
        return new ByteArrayResourceObject(name + extension, name + resolution + extension, content, timestamp);
    }

}
