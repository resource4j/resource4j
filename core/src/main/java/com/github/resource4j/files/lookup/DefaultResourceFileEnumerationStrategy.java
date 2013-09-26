package com.github.resource4j.files.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefaultResourceFileEnumerationStrategy implements ResourceFileEnumerationStrategy {

    private static final String EMPTY_SECTION = "";

    private static final String[] EMPTY_CONTEXT_OPTIONS = new String[] { EMPTY_SECTION };

    private char localeSectionSeparator = '_';

    private char sectionSeparator = '-';

    private ResourceContextProvider contextProvider;

    public char getLocaleSectionSeparator() {
        return localeSectionSeparator;
    }

    public void setLocaleSectionSeparator(char localeSectionSeparator) {
        this.localeSectionSeparator = localeSectionSeparator;
    }

    public char getSectionSeparator() {
        return sectionSeparator;
    }

    public void setSectionSeparator(char sectionSeparator) {
        this.sectionSeparator = sectionSeparator;
    }

    public ResourceContextProvider getContextProvider() {
        return contextProvider;
    }

    public void setContextProvider(ResourceContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public List<String> enumerateFileNameOptions(String[] fileNames, Locale locale) {
        List<String> nameOptions = new ArrayList<String>();
        String[] contextOptions = EMPTY_CONTEXT_OPTIONS;
        if (contextProvider != null) {
            String context = contextProvider.getResourceContext();
            if ((context != null) && (context.length() > 0)) {
                contextOptions = new String[] { context, EMPTY_SECTION };
            }
        }
        List<String> localeOptions = enumerateLocaleOptions(locale);
        for (String localeOption : localeOptions) {
            for (String fileName : fileNames) {
                for (String contextOption : contextOptions) {
                    int dot = fileName.indexOf('.');
                    StringBuilder builder = new StringBuilder();
                    if (dot >= 0) {
                        builder.append(fileName.substring(0, dot));
                    } else {
                        builder.append(fileName);
                    }
                    if (contextOption.length() > 0) {
                        if (builder.length() > 0) {
                            builder.append(sectionSeparator);
                        }
                        builder.append(contextOption);
                    }
                    if (localeOption.length() > 0) {
                        if (builder.length() > 0) {
                            builder.append(sectionSeparator);
                        }
                        builder.append(localeOption);
                    }
                    if ((dot >= 0) && (dot < fileName.length()-1)) {
                        builder.append('.');
                        builder.append(fileName.substring(dot+1));
                    }
                    nameOptions.add(builder.toString());
                }
            }
        }
        return nameOptions;
    }

    public List<String> enumerateLocaleOptions(Locale locale) {
        List<String> localeOptions = new ArrayList<String>();
        if (locale != null) {
            String compact = locale.getLanguage();
            String medium = compact+localeSectionSeparator+locale.getCountry();
            String full = medium + localeSectionSeparator + locale.getVariant();
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
