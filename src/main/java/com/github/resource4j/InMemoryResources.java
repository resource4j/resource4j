package com.github.resource4j;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.ImageIcon;

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
    }

    @Override
    protected String lookup(ResourceKey key, Locale locale) {
        return null;
    }

    @Override
    public ResourceFile contentOf(ResourceKey key, Locale locale) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ImageIcon lookupIcon(Locale locale, String iconName) {
        // TODO Auto-generated method stub
        return null;
    }

}
