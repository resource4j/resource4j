package com.github.resource4j.resources;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;

import java.util.Locale;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;


public class GenericResourceProvider implements ResourceProvider {

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
    	return get(name, in(locale));
    }

    @Override
    public ResourceFile contentOf(String name, Locale locale) {
        return resources.contentOf(name, locale);
    }

	@Override
	public OptionalString get(String name, ResourceResolutionContext context) {
        OptionalString result = null;
        for (ResourceKey bundle : bundles) {
            OptionalString string = resources.get(bundle.child(name), context);
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
	public ResourceFile contentOf(String name, ResourceResolutionContext context) {
		return resources.contentOf(name, context);
	}

}
