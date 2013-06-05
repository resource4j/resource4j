package com.github.resource4j.files;

import java.io.InputStream;

public interface ResourceParser<T> {

    T parse(InputStream stream);

}
