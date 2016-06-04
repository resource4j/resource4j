package com.github.resource4j.spring.test;

import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.ResourcesConfiguratorBuilder;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.SpringELValuePostProcessor;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import com.github.resource4j.spring.context.RequestResolutionContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.github.resource4j.resources.ResourcesConfiguratorBuilder.configure;

@Configuration
@Import(TestContextConfiguration.class)
public class TestConfiguration {

    @Autowired
    private ResourceValueBeanPostProcessor postProcessor;

    @Bean
    public RequestResolutionContextProvider contextProvider() {
        return new RequestResolutionContextProvider("WEB");
    }

    @Bean
    public ResourceObjectProvider springObjectProvider() {
        return new SpringResourceObjectProvider();
    }

    @Bean
    public ResourceValuePostProcessor valuePostProcessor() {
        return new SpringELValuePostProcessor();
    }

    @Bean
    public Resources resources() {
        ResourcesConfiguratorBuilder.Configurator configurator = configure()
                .sources(springObjectProvider())
                .postProcessingBy(valuePostProcessor())
                .get();
        Resources resources = new RefreshableResources(configurator);
        return resources;
    }

}
