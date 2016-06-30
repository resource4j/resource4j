package com.github.resource4j.spring.config;

import com.github.resource4j.objects.providers.ClasspathResourceObjectProvider;
import com.github.resource4j.objects.providers.MappingResourceObjectProvider;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.SpringELValuePostProcessor;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.resource4j.objects.providers.ResourceObjectProviders.bind;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;

@Configuration
public class Resource4jAutoConfiguration {

    @Autowired(required = false)
    private RefreshableResourcesConfigurator configuration;

    @Bean
    public static ResourceValueBeanPostProcessor resource4jAutowiringPostProcessor() {
        return new ResourceValueBeanPostProcessor();
    }

    @Lazy
    @Bean
    public static SpringELValuePostProcessor springELValuePostProcessor() {
        return new SpringELValuePostProcessor();
    }

    @Lazy
    @Bean
    public static SpringResourceObjectProvider springResourceObjects() {
        return new SpringResourceObjectProvider();
    }

    @Bean
    @ConditionalOnMissingBean(Resources.class)
    public Resources resources(SpringResourceObjectProvider springResourceObjects) {
        if (configuration == null) {
            configuration = configure()
                    .sources(patternMatching()
                                .when(".+\\.properties$", classpath())
                                .otherwise(springResourceObjects))
                    .get();
        }
        return new RefreshableResources(configuration);
    }

}
