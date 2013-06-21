package com.github.resource4j.files.lookup;

import java.util.List;
import java.util.Locale;

public interface ResourceFileEnumerationStrategy {

    List<String> enumerateFileNameOptions(String[] fileNames, Locale locale);

}
