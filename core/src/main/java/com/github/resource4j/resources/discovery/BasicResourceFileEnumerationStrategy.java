package com.github.resource4j.resources.discovery;

import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Supports only resolution contexts of form {Locale, String}
 * @author Ivan Gammel
 * @since 1.1 (as DefaultResourceFileEnumerationStrategy)
 */
public class BasicResourceFileEnumerationStrategy implements ResourceFileEnumerationStrategy {

    public static final char DEFAULT_COMPONENT_SEPARATOR = '-';

	public static final char DEFAULT_SECTION_SEPARATOR = '_';

    private char sectionSeparator = DEFAULT_SECTION_SEPARATOR;

    private char componentSeparator = DEFAULT_COMPONENT_SEPARATOR;

    public char getSectionSeparator() {
        return sectionSeparator;
    }

    public void setSectionSeparator(char sectionSeparator) {
        this.sectionSeparator = sectionSeparator;
    }

    public char getComponentSeparator() {
        return componentSeparator;
    }

    public void setComponentSeparator(char componentSeparator) {
        this.componentSeparator = componentSeparator;
    }

	@Override
	public List<String> enumerateFileNameOptions(String[] fileNames, ResourceResolutionContext context) {
		List<String> result = new ArrayList<>();
		ResourceResolutionComponent[] components = context.components();
		int index = components.length - 1;
		for (String filename : fileNames) {
			int dot = filename.lastIndexOf('.');
			String prefix = dot >= 0 ? filename.substring(0, dot) : filename;
			String suffix = dot >= 0 ? filename.substring(dot) : "";
			if (index >= 0) {
				enumerate(prefix, suffix, result, components, index);
			}
			result.add(prefix + suffix);
		}
		return result;
	}

	private void enumerate(String prefix, String suffix, List<String> result, ResourceResolutionComponent[] components, int index) {
		List<String> sections = components[index].sections();
		enumerateSections(result, prefix, suffix, components, index, sections, 0, "");
		if (index > 0) {
			enumerate(prefix, suffix, result, components, index - 1);
		}
	}

	private void enumerateSections(List<String> result, String prefix,
			String suffix, ResourceResolutionComponent[] components, int index,
			List<String> sections, int sectionIndex, String sectionPrefix) {
		boolean separatorRequired = sectionPrefix.length() > 0; 
		String newPrefix =  sectionPrefix + (separatorRequired ? sectionSeparator : "") + sections.get(sectionIndex);
		if (sectionIndex < sections.size() - 1) {
			enumerateSections(result, prefix, suffix, components, index, sections, sectionIndex + 1, newPrefix);
		}
		if (index > 0) {
			enumerate(prefix, componentSeparator + newPrefix + suffix, result, components, index - 1);
		}
		result.add(prefix + (prefix.length() > 0 ? componentSeparator : "") + newPrefix + suffix);
	}


}
