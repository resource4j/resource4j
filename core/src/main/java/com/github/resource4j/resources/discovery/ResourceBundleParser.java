package com.github.resource4j.resources.discovery;

import java.util.Map;

import com.github.resource4j.ResourceObject;

/**
 * Helper interface for parsing resource objects into key-value bundles.
 * Concrete implementations work with different data formats.
 * @author Ivan Gammel
 */
public interface ResourceBundleParser {

    /**
     * Parses the given resource object into key-value map. Its actual name may differ
     * @param object the object to parse
     * @return map of key-value pairs stored in given file
     */
    Map<String,String> parse(ResourceObject object);

}
