package com.github.resource4j.resources.discovery;

import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.List;

public interface ResourceFileEnumerationStrategy {

    List<String> enumerateFileNameOptions(String[] fileNames, ResourceResolutionContext context);

}
