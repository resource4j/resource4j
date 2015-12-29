package com.github.resource4j.generic.objects;

import com.github.resource4j.InaccessibleResourceException;
import com.github.resource4j.MissingResourceObjectException;
import com.github.resource4j.ResourceException;
import com.github.resource4j.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * Supports objects sizes no larger than <code>{@link IO#MAX_SIZE}-1</code> bytes.
 */
public class URIResourceObject extends AbstractResourceObject {

    private static final Logger LOG = LoggerFactory.getLogger(URIResourceObject.class);

    private final URI uri;

    private long timestamp;

    public URIResourceObject(URI uri, String name, String resolvedName, long timestamp) {
        super(name, resolvedName);
        this.uri = uri;
        this.timestamp = timestamp;
    }

    @Override
    public long size() {
        long length = 0;
        LOG.error("Invoking size() on remote ({}) resource object {} ({}): potential DoS attack vector",
                uri.getScheme(),
                name,
                resolvedName);
        try (InputStream is = uri.toURL().openStream()) {
            length = IO.size(is);
        } catch (IOException e) {
            throw new InaccessibleResourceException(e, name, resolvedName);
        }
        return length;
    }

    @Override
    public long lastModified() {
        return timestamp;
    }

    @Override
    public InputStream asStream() {
        try {
            return uri.toURL().openStream();
        } catch (IOException | SecurityException e) {
            throw new InaccessibleResourceException(e, name, resolvedName);
        }
    }
}
