package com.github.resource4j.resources;

import static com.github.resource4j.ResourceKey.bundle;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

/**
 *
 * @author Ivan Gammel
 */
public class InMemoryResources extends AbstractResources implements EditableResources {

    private Map<Locale, Map<ResourceKey,String>> storage = new HashMap<Locale, Map<ResourceKey, String>>();

    @Override
    public void put(ResourceKey key, Locale locale, Object value) {
        Map<ResourceKey, String> localeResources = storage.get(locale);
        if (localeResources == null) {
            localeResources = new HashMap<ResourceKey, String>();
            storage.put(locale, localeResources);
        }
    }

    @Override
    public void remove(ResourceKey key, Locale locale) {
        Map<ResourceKey, String> localeResources = storage.get(locale);
        if (localeResources == null) {
            return;
        }
        localeResources.remove(key);
    }

    @Override
    protected String lookup(ResourceKey key, Locale locale) {
        Map<ResourceKey, String> localeResources = storage.get(locale);
        if (localeResources == null) {
            return null;
        }
        return localeResources.get(key);
    }

    @Override
    public ResourceFile contentOf(String name, Locale locale) {
        throw new MissingResourceFileException(bundle(name));
    }


}
