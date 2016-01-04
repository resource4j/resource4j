package com.github.resource4j.objects.providers;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.MissingResourceObjectException;

/**
 * This resource object provider provides a mechanism to load resource objects from different contexts using pattern
 * matching. E.g. you can set ".properties" files to be loaded from classpath, ".html" files to be loaded
 * from web application root folder etc.
 * 
 * @author Ivan Gammel
 */
public class MappingResourceObjectProvider implements ResourceObjectProvider {

	private List<Mapping> mappings;
	
	/**
	 * Specify list of pattern to resource object provider mappings. Note, that if the actual object name
	 * matches two or more patterns, first one in the map will be used. You may use {@link LinkedHashMap} 
	 * to specify exact order.
	 * @param mappings a set of pattern-factory pairs stored in {@link Map}
	 */
	public void setMappings(Map<String,ResourceObjectProvider> mappings) {
		this.mappings = new ArrayList<>(mappings.size());
		for (Map.Entry<String, ResourceObjectProvider> entry : mappings.entrySet()) {
			this.mappings.add(new Mapping(compile(entry.getKey()), entry.getValue()));
		}
	}
	
	@Override
	public ResourceObject get(String name, String actualName)
			throws MissingResourceObjectException {
		for (Mapping mapping : mappings) {
			if (mapping.pattern.matcher(actualName).matches()) {
				return mapping.factory.get(name, actualName);
			}
		}
		throw new MissingResourceObjectException(name, actualName);
	}

	private static class Mapping {

		public Pattern pattern;
		
		public ResourceObjectProvider factory;

		public Mapping(Pattern pattern, ResourceObjectProvider factory) {
			super();
			this.pattern = pattern;
			this.factory = factory;
		}
	}
	
}
