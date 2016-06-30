package com.github.resource4j.resources;

import com.github.resource4j.objects.parsers.BundleParser;
import com.github.resource4j.resources.discovery.ContentType;
import com.github.resource4j.resources.impl.ResolvedName;

public class BundleFormat {

    private final String extension;

    private final String mimeType;

    private final BundleParser parser;

    public static BundleFormat format(BundleParser parser) {
        ContentType contentType = contentTypeOf(parser);
        return new BundleFormat(parser, contentType.extension(), contentType.mimeType());
    }

    public static BundleFormat format(BundleParser parser, String extension) {
        ContentType contentType = contentTypeOf(parser);
        return new BundleFormat(parser, extension, contentType.mimeType());
    }

    public static BundleFormat format(BundleParser parser, String extension, String mimeType) {
        return new BundleFormat(parser, extension, mimeType);
    }

    private static ContentType contentTypeOf(BundleParser parser) {
        Class<? extends BundleParser> parserClass = parser.getClass();
        ContentType contentType = parserClass.getAnnotation(ContentType.class);
        if (contentType == null) {
            throw new IllegalArgumentException("Unable to detect extension and content type for parser " + parserClass.getSimpleName());
        }
        return contentType;
    }

    public BundleFormat(BundleParser parser, String extension, String mimeType) {
        this.parser = parser;
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public ResolvedName applyTo(ResolvedName bundleName) {
        String name = bundleName.name() + (extension() != null ? extension() : "");
        return new ResolvedName(name, bundleName.context());
    }

    public BundleParser parser() {
        return parser;
    }

    public String mimeType() {
        return mimeType;
    }

    public String extension() {
        return extension;
    }

    public String toString() {
        return parser.getClass().getSimpleName() +
                (extension() != null && mimeType() != null ?
                "(" + extension() + ", " + mimeType() + ")" :
                "");
    }

}
