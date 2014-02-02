package com.github.resource4j.files.lookup;

import java.util.List;

import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public interface ResourceFileEnumerationStrategy {

    List<String> enumerateFileNameOptions(String[] fileNames, ResourceResolutionContext context);

}
