package com.github.resource4j.files.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;

public interface ResourceParser<T, V extends OptionalValue<T>> {

    V parse(ResourceKey key, ResourceFile file);

}
