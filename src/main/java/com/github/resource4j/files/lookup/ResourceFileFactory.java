package com.github.resource4j.files.lookup;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

public interface ResourceFileFactory {

    ResourceFile getFile(ResourceKey key, String actualName) throws MissingResourceFileException;

}
