package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;

import java.io.File;
import java.time.Clock;
import java.util.LinkedHashMap;

public class ResourceObjectProviders {

    public static ClasspathResourceObjectProvider classpath() {
        return new ClasspathResourceObjectProvider();
    }

    public static ClasspathResourceObjectProvider classpathOf(ClassLoader loader) {
        return new ClasspathResourceObjectProvider(loader);
    }

    public static FileResourceObjectRepository filesIn(File folder) {
        return new FileResourceObjectRepository(folder, Clock.systemUTC());
    }

    public static HeapResourceObjectRepository inHeap() {
        return new HeapResourceObjectRepository(Clock.systemUTC());
    }

    public static PatternMatchingConfigurator patternMatching() {
        return new PatternMatchingConfigurator();
    }

    public static FilteringResourceObjectProviderDSL bind(ResourceObjectProvider provider) {
        return new FilteringResourceObjectProviderDSL(provider);
    }

    public final static class FilteringResourceObjectProviderDSL {

        private ResourceObjectProvider provider;

        private FilteringResourceObjectProviderDSL(ResourceObjectProvider provider) {
            this.provider = provider;
        }

        public FilteringResourceObjectProvider to(String path) {
            return new FilteringResourceObjectProvider(provider, path);
        }

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
