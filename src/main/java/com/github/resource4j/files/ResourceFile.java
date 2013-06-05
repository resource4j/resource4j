package com.github.resource4j.files;

import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

public interface ResourceFile {

    ResourceKey key();

    InputStream asStream();

    <T> OptionalValue<T> parsedTo(ResourceParser<T> parser);

}
