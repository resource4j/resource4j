package com.github.resource4j.files.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.resource4j.resources.resolution.*;

/**
 * Supports only resolution contexts of form {Locale, String}
 * @author Ivan Gammel
 * @since 1.1 (as DefaultResourceFileEnumerationStrategy)
 */
public class BasicResourceFileEnumerationStrategy implements ResourceFileEnumerationStrategy {

    public static final char DEFAULT_COMPONENT_SEPARATOR = '-';

	public static final char DEFAULT_SECTION_SEPARATOR = '_';

	private static final String EMPTY_SECTION = "";

    private static final String[] EMPTY_CONTEXT_OPTIONS = new String[] { EMPTY_SECTION };

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
		ResourceResolutionComponent[] components = context.components();
		StringBuilder before = new StringBuilder();
		Locale locale = null;
		StringBuilder after = new StringBuilder();
		for (ResourceResolutionComponent component : components) {
			if (component instanceof LocaleResolutionComponent) {
				locale = ((LocaleResolutionComponent) component).locale();
			} else {
				StringBuilder current = locale == null ? before : after;
				StringBuilder sections = new StringBuilder();
				for (String section : component.sections()) {
					if (sections.length() > 0) sections.append(sectionSeparator);
					sections.append(section);
				}
				if (current.length() > 0) current.append(componentSeparator);
				current.append(sections);
			}
		}
		return enumerateFileNameOptions(fileNames, before.toString(), locale, after.toString());
	}

    public List<String> enumerateFileNameOptions(String[] fileNames, String before, Locale locale, String after) {
        List<String> nameOptions = new ArrayList<String>();
        String[] beforeOptions = EMPTY_CONTEXT_OPTIONS;
        if (before.length() > 0) {
            beforeOptions = new String[] { before, EMPTY_SECTION };
        }
        String[] afterOptions = EMPTY_CONTEXT_OPTIONS;
        if (after.length() > 0) {
            afterOptions = new String[] { after, EMPTY_SECTION };
        }
        List<String> localeOptions = enumerateLocaleOptions(locale);
        for (String beforeOption : beforeOptions) {
	        for (String localeOption : localeOptions) {
	            for (String fileName : fileNames) {
	                for (String afterOption : afterOptions) {
	                    int dot = fileName.indexOf('.');
	                    StringBuilder builder = new StringBuilder();
	                    if (dot >= 0) {
	                        builder.append(fileName.substring(0, dot));
	                    } else {
	                        builder.append(fileName);
	                    }
	                    if (beforeOption.length() > 0) {
	                        if (builder.length() > 0) {
	                            builder.append(componentSeparator);
	                        }
	                        builder.append(beforeOption);
	                    }
	                    if (localeOption.length() > 0) {
	                        if (builder.length() > 0) {
	                            builder.append(componentSeparator);
	                        }
	                        builder.append(localeOption);
	                    }
	                    if (afterOption.length() > 0) {
	                        if (builder.length() > 0) {
	                            builder.append(componentSeparator);
	                        }
	                        builder.append(afterOption);
	                    }
	                    if ((dot >= 0) && (dot < fileName.length()-1)) {
	                        builder.append('.');
	                        builder.append(fileName.substring(dot+1));
	                    }
	                    nameOptions.add(builder.toString());
	                }
	            }
	        }
        }
        return nameOptions;
    }

    public List<String> enumerateLocaleOptions(Locale locale) {
        List<String> localeOptions = new ArrayList<String>();
        if (locale != null) {
            String compact = locale.getLanguage();
            String medium = compact + sectionSeparator + locale.getCountry();
            String full = medium + sectionSeparator + locale.getVariant();
            if (!locale.getVariant().isEmpty()) {
                localeOptions.add(full);
            }
            if (!locale.getCountry().isEmpty()) {
                localeOptions.add(medium);
            }
            if (!locale.getLanguage().isEmpty()) {
                localeOptions.add(compact);
            }
        }
        localeOptions.add(EMPTY_SECTION);
        return localeOptions;
    }

}
