package com.github.resource4j.resources.discovery;

import java.util.List;

import com.github.resource4j.resources.context.ResourceResolutionContext;

public interface ResourceFileEnumerationStrategy {

    List<String> enumerateFileNameOptions(String[] fileNames, ResourceResolutionContext context);

}
