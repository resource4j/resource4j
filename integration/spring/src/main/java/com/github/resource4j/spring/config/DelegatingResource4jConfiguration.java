package com.github.resource4j.spring.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.resource4j.files.lookup.ClasspathResourceFileFactory;
import com.github.resource4j.files.lookup.MappingResourceFileFactory;
import com.github.resource4j.files.lookup.ResourceFileFactory;
import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.SpringResourceFileFactory;

@Configuration
public class DelegatingResource4jConfiguration extends Resource4jConfigurationSupport implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Bean
	public ResourceFileFactory fileFactory() {
		MappingResourceFileFactory factory = new MappingResourceFileFactory();
		Map<String, ResourceFileFactory> mappings = new LinkedHashMap<>();
		mappings.put(".+\\.properties$", new ClasspathResourceFileFactory());
		SpringResourceFileFactory springResourceFactory = new SpringResourceFileFactory();
		springResourceFactory.setApplicationContext(applicationContext);
		mappings.put(".+", springResourceFactory);
		factory.setMappings(mappings);
		return factory;
	}
	
	@Bean
	public Resources resources() {
		DefaultResources resources = new DefaultResources();
		
		return resources;
	}
	
	@Bean
	public ResourceValueBeanPostProcessor resourceValueBeanPostProcessor() {
		return new ResourceValueBeanPostProcessor();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
