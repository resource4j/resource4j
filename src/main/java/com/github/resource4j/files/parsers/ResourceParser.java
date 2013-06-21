package com.github.resource4j.files.parsers;

import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

public interface ResourceParser<T, V extends OptionalValue<T>> {

    V parse(ResourceKey key, InputStream stream);

}
