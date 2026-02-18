package com.github.resource4j.resources;

import com.github.resource4j.objects.parsers.BundleParser;
import com.github.resource4j.resources.discovery.ContentType;
import com.github.resource4j.resources.impl.ResolvedName;

public record BundleFormat(BundleParser parser, String extension, String mimeType) {

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

    public ResolvedName applyTo(ResolvedName bundleName) {
        String name = bundleName.name() + (extension() != null ? extension() : "");
        return new ResolvedName(name, bundleName.context());
    }

    @Override
    public String toString() {
        return parser.getClass().getSimpleName() +
                (extension() != null && mimeType() != null ?
                "(" + extension() + ", " + mimeType() + ")" :
                "");
    }

}
