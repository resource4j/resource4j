package com.github.resource4j.files.lookup;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;

public class MappingResourceFileFactory implements ResourceFileFactory {

	private List<Mapping> mappings;
	
	/**
	 * NB: this method relies on LinkedHashMap as default
	 * @param mappings
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
