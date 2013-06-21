package com.github.resource4j.files.lookup;

import java.util.Map;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;

public interface ResourceBundleParser {

    String getResourceFileName(ResourceKey key);

    Map<String,String> parse(ResourceFile file);

}
