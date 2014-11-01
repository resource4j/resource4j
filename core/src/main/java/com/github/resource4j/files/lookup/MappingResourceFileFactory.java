package com.github.resource4j.files.lookup;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

/**
 * This file factory provides a mechanism to load resource files from different contexts using pattern matching.
 * E.g. you can set ".properties" files to be loaded from classpath, ".html" files to be loaded 
 * from web appplication root folder etc.
 * 
 * @author Ivan Gammel
 */
public class MappingResourceFileFactory implements ResourceFileFactory {

	private List<Mapping> mappings;
	
	/**
	 * Specify list of pattern to resource file factory mappings. Note, that if the actual file name 
	 * matches two or more patterns, first one in the map will be used. You may use {@link LinkedHashMap} 
	 * to specify exact order.
	 * @param mappings a set of pattern-factory pairs stored in {@link Map}
	 */
	public void setMappings(Map<String,ResourceFileFactory> mappings) {
		this.mappings = new ArrayList<>(mappings.size());
		for (Map.Entry<String, ResourceFileFactory> entry : mappings.entrySet()) {
			this.mappings.add(new Mapping(compile(entry.getKey()), entry.getValue()));
		}
	}
	
	@Override
	public ResourceFile getFile(ResourceKey key, String actualName)
			throws MissingResourceFileException {
		for (Mapping mapping : mappings) {
			if (mapping.pattern.matcher(actualName).matches()) {
				return mapping.factory.getFile(key, actualName);
			}
		}
		throw new MissingResourceFileException(key, actualName);
	}

	private static class Mapping {

		public Pattern pattern;
		
		public ResourceFileFactory factory;

		public Mapping(Pattern pattern, ResourceFileFactory factory) {
			super();
			this.pattern = pattern;
			this.factory = factory;
		}
		
	}
	
}
