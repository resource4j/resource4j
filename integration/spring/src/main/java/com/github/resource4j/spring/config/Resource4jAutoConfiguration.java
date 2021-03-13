package com.github.resource4j.spring.config;

import com.github.resource4j.resources.BundleFormat;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.Resource4jMessageSource;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.SpringELValuePostProcessor;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;
import static com.github.resource4j.objects.providers.resolvers.DefaultObjectNameResolver.javaPropertiesLocaleResolver;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.processors.BasicValuePostProcessor.macroSubstitution;

@Configuration
public class Resource4jAutoConfiguration {

    private RefreshableResourcesConfigurator configuration;

    public Resource4jAutoConfiguration(@Autowired(required = false) RefreshableResourcesConfigurator configuration) {
        this.configuration = configuration;
    }

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
        return new SpringResourceObjectProvider().with(javaPropertiesLocaleResolver());
    }

    @Lazy
    @Bean
    public static BundleFormat defaultFormat() {
        return BundleFormat.format(propertyMap());
    }

    @Bean
    @ConditionalOnMissingBean(Resources.class)
    public Resources resources(SpringResourceObjectProvider springResourceObjects,
                               List<BundleFormat> formats) {
        if (configuration == null) {
            configuration = configure()
                    .formats(formats.toArray(new BundleFormat[0]))
                    .sources(patternMatching()
                                .when(".+\\.properties$", classpath().with(springResourceObjects::resolver))
                                .otherwise(springResourceObjects))
                    .postProcessingBy(macroSubstitution())
                    .get();
        }
        return new RefreshableResources(configuration);
    }

    @Bean
    public MessageSource messageSource(Resources resources) {
        return new Resource4jMessageSource(resources);
    }

}
