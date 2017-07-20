package com.github.resource4j.resources;

import com.github.resource4j.resources.impl.ResolvedKey;

public interface ParametrizedKeyBuilder {

    String forResolvedKey(ResolvedKey key, String declaration);

    String forDeclaredParams(ResolvedKey key);

}
