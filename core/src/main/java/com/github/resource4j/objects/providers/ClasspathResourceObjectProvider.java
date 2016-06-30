package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.FileResourceObject;
import com.github.resource4j.objects.URIResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import com.github.resource4j.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Clock;

public class ClasspathResourceObjectProvider extends AbstractFileResourceObjectProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClasspathResourceObjectProvider.class);

    private Clock clock;

    private ClassLoader loader;
	
	public ClasspathResourceObjectProvider() {
        this(Resources.class.getClassLoader(), Clock.systemUTC());
	}

    public ClasspathResourceObjectProvider(ClassLoader loader) {
        this(loader, Clock.systemUTC());
    }

    public ClasspathResourceObjectProvider(ClassLoader loader, Clock clock) {
        if (loader == null) {
            throw new NullPointerException("loader");
        }
        if (clock == null) {
            throw new NullPointerException("clock");
        }
        this.loader = loader;
        this.clock = clock;
        LOG.debug("Resource path configured: <classpath> [" + loader.toString() + "]");
    }

    @Override
    public ResourceObject get(String name, String resolvedName) throws MissingResourceObjectException {
        try {
            URL url = loader.getResource(resolvedName);
            if (url == null) {
                throw new MissingResourceObjectException(name, resolvedName);
            }
            URI uri = url.toURI();
            switch (uri.getScheme()) {
                case "file":
                    return new FileResourceObject(name, new File(url.toURI()));
                default:
                    return new URIResourceObject(url.toURI(), name, resolvedName, clock.millis());
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid resource name " + name, e);
        }
    }

    @Override
    public String toString() {
        return "classpath:" + loader.toString();
    }

}
