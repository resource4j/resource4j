package com.github.resource4j.objects.providers;

import javafx.beans.binding.Bindings;

import java.io.File;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ResourceObjectProviders {

    public static FileResourceObjectRepository filesIn(File folder) {
        return new FileResourceObjectRepository(folder, Clock.systemUTC());
    }

    public static HeapResourceObjectRepository inHeap() {
        return new HeapResourceObjectRepository(Clock.systemUTC());
    }


    public static PatternMatchingConfigurator patternMatching() {
        return new PatternMatchingConfigurator();
    }

    public static class PatternMatchingConfigurator {

        private static final String MATCH_ALL = ".+";
        private LinkedHashMap<String, ResourceObjectProvider> providers = new LinkedHashMap<>();

        public PatternMatchingConfigurator when(String pattern, ResourceObjectProvider provider) {
            providers.put(pattern, provider);
            return this;
        }

        public MappingResourceObjectProvider otherwise(ResourceObjectProvider provider) {
            providers.put(MATCH_ALL, provider);
            MappingResourceObjectProvider result = new MappingResourceObjectProvider();
            result.setMappings(providers);
            return result;
        }


        public MappingResourceObjectProvider otherwise(String pattern, ResourceObjectProvider provider) {
            providers.put(pattern, provider);
            MappingResourceObjectProvider result = new MappingResourceObjectProvider();
            result.setMappings(providers);
            return result;
        }
    }

}
