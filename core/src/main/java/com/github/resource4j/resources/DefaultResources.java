package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.resources.cache.CachedValue.cached;
import static com.github.resource4j.resources.cache.CachedValue.missingValue;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.cache.CachedValue;
import com.github.resource4j.resources.cache.ResourceCache;
import com.github.resource4j.resources.cache.SimpleResourceCache;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class DefaultResources extends CustomizableResources {

    /**
     *
     */
    public final static ResourceKey DEFAULT_APPLICATION_RESOURCES = bundle("i18n.resources");

    private ResourceCache<String> valueCache = new SimpleResourceCache<String>();

    private ResourceCache<ResourceFile> fileCache = new SimpleResourceCache<ResourceFile>();

    public DefaultResources() {
        this.defaultResourceBundle = DEFAULT_APPLICATION_RESOURCES;
    }

    public DefaultResources(String defaultBundle) {
        this.defaultResourceBundle = ResourceKey.bundle(defaultBundle);
    }

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    protected String lookup(ResourceKey key, Locale locale) {
        // first, try to get the value from cache
        CachedValue<String> cachedValue = valueCache.get(key, locale);
        if (cachedValue != null) {
            return cachedValue.get();
        }

        // value not in cache, load related bundles to cache
        synchronized (valueCache) {
            String bundleName = bundleParser.getResourceFileName(key);
            String defaultBundleName = bundleParser.getResourceFileName(defaultResourceBundle);
            String[] bundleOptions = bundleName != null
                    ? new String[] { bundleName, defaultBundleName }
                    : new String[] { defaultBundleName };
            List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(bundleOptions, locale);
            for (String option : options) {
                try {
                    ResourceFile file = fileFactory.getFile(key, option);
                    Map<String, String> properties = bundleParser.parse(file);
                    for (Map.Entry<String, String> property : properties.entrySet()) {
                        String bundle = key.getBundle();
                        String id = property.getKey();
                        ResourceKey propertyKey = bundle(bundle).child(id);
                        String propertyValue = property.getValue();
                        valueCache.putIfAbsent(propertyKey, locale, cached(propertyValue));
                    }
                } catch (MissingResourceFileException e) {
                }
            }
        }
        // finally, try again - we might load the value during the caching of the bundles
        cachedValue = valueCache.get(key, locale);
        if (cachedValue != null) {
            return cachedValue.get();
        }

        // value not found: record this search result to cache
        valueCache.put(key, locale, missingValue(String.class));
        return null;
    }

    @Override
    public ResourceFile contentOf(String name, Locale locale) {
        ResourceKey key = bundle(name);
        // first, try to find the file in cache
        CachedValue<ResourceFile> cachedFile = fileCache.get(key, locale);
        if (cachedFile != null) {
            if (cachedFile.isMissing()) {
                throw new MissingResourceFileException(key);
            } else {
                return cachedFile.get();
            }
        }
        // not cached, try to find it
        List<String> options = fileEnumerationStrategy.enumerateFileNameOptions(new String[] { name }, locale);
        for (String option : options) {
            try {
                ResourceFile file = fileFactory.getFile(key, option);
                file.asStream().close();
                fileCache.put(key, locale, cached(file));
                return file;
            } catch (MissingResourceFileException e) {
            } catch (IOException e) {
            }
        }
        // file not found: record this search result in cache
        fileCache.put(key, locale, missingValue(ResourceFile.class));
        throw new MissingResourceFileException(key);
    }
}
