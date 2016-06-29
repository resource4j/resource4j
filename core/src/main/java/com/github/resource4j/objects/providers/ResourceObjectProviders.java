package com.github.resource4j.objects.providers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.providers.mutable.FileResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;

import java.io.File;
import java.time.Clock;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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

    public static ResourceObjectProviderAdaptDSL bind(ResourceObjectProvider provider) {
        return new ResourceObjectProviderAdaptDSL(provider);
    }

    public static class ResourceObjectProviderAdaptDSL {
        private ResourceObjectProvider provider;

        public ResourceObjectProviderAdaptDSL(ResourceObjectProvider provider) {
            this.provider = provider;
        }

        public ResourceObjectProviderAdapter to(String path) {
            return new ResourceObjectProviderAdapter(provider, path);
        }
    }

    public static class ResourceObjectProviderAdapter implements ResourceObjectProvider {

        private ResourceObjectProvider provider;

        private String basePath;

        public ResourceObjectProviderAdapter(ResourceObjectProvider provider, String basePath) {
            this.provider = provider;
            this.basePath = basePath;
        }

        @Override
        public ResourceObject get(String name, ResourceResolutionContext context) throws ResourceObjectAccessException {
            String realName = basePath + (name.startsWith("/") ? name : '/' + name);
            return provider.get(realName, context);
        }

        public List<ResourceObjectProvider> unwrap() {
            return Arrays.asList(provider);
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
