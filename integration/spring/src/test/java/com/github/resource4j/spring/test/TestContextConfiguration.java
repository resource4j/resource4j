package com.github.resource4j.spring.test;

import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContextConfiguration {

    @Bean
    public ResourceValueBeanPostProcessor resourceAnnotationPostProcessor() {
        return new ResourceValueBeanPostProcessor();
    }
}
