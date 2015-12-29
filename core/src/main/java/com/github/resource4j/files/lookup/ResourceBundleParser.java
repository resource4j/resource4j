package com.github.resource4j.files.lookup;

import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.resources.CustomizableResources;

/**
 * Helper interface for parsing resource objects into key-value bundles. Concrete implementations work with different data formats.
 * @author Ivan Gammel
 */
public interface ResourceBundleParser {

    /**
     * Constructs a resource file name which contains data for specific resource key. This name may be transformed by a
     * resource loader implementation (see {@link CustomizableResources}) to use the specific resource file for given
     * locale and other context settings.
     * @param key a key to get resource file name for
     * @return the resource file name or <code>null</code> if file not found.
     */
    String getResourceFileName(ResourceKey key);

    /**
     * Parses the given resource object into key-value map. Its actual name may differ
     * @param object the object to parse
     * @return map of key-value pairs stored in given file
     */
    Map<String,String> parse(ResourceObject object);

}
