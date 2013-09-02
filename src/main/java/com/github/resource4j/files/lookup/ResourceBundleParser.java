package com.github.resource4j.files.lookup;

import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.CustomizableResources;

/**
 * Helper interface for parsing resource files. Concrete implementations work with different file formats.
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
     * Parses the given resource file. Its actual name may differ
     * @param file
     * @return
     */
    Map<String,String> parse(ResourceFile file);

}
