package com.github.resource4j.demo;

import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static com.github.resource4j.objects.providers.ResourceObjectProviders.bind;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;

@Configuration
public class I18nConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ResourceValueRepository i18nDatabase() {
        return new HeapResourceObjectRepository(clock());
    }

    @Bean
    public RefreshableResourcesConfigurator resourcesConfiguration(SpringResourceObjectProvider springResourceObjects) {
        return configure().sources(
                patternMatching()
                    .when(".+\\.properties$", bind(springResourceObjects).to("classpath:/i18n"))
                    .otherwise(bind(springResourceObjects).to("classpath:/templates")),
                i18nDatabase())
                .get();
    }

}
