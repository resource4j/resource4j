package com.github.resource4j.generic;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceProvider;
import com.github.resource4j.Resources;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;


public class GenericResourceProvider implements ResourceProvider {

    private final static Logger LOG = LoggerFactory.getLogger(GenericResourceProvider.class);

    private Resources resources;

    private ResourceKey[] bundles;

    public GenericResourceProvider(Resources resources, ResourceKey resourceKey, ResourceKey... defaultKeys) {
        super();
        this.resources = resources;
        this.bundles = new ResourceKey[defaultKeys.length + 1];
        this.bundles[0]  = resourceKey;
        for (int i = 0; i < defaultKeys.length; i++) {
            this.bundles[i+1] = defaultKeys[i];
        }
    }

    @Override
    public OptionalString get(String name, Locale locale) {
        OptionalString result = null;
        for (ResourceKey bundle : bundles) {
            OptionalString string = resources.get(bundle.child(name), locale);
            if (string.asIs() != null) {
                result = string;
                break;
            } else if (result == null) {
                result = string;
            }
        }
        return result;
    }

    @Override
    public ResourceFile contentOf(String name, Locale locale) {
        for (ResourceKey bundle : bundles) {
            ResourceKey key = bundle.child(name);
            try {
                return resources.contentOf(key, locale);
            } catch (MissingResourceFileException e) {
                LOG.trace("Missing resource file: {}", key);
            }
        }
        throw new MissingResourceFileException(bundles[0].child(name));
    }

}
