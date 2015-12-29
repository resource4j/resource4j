package com.github.resource4j.spring.config;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.resource4j.generic.objects.factory.ClasspathResourceObjectFactory;
import com.github.resource4j.generic.objects.factory.ResourceObjectFactory;
import com.github.resource4j.spring.SpringResourceObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.resource4j.generic.objects.factory.MappingResourceObjectFactory;
import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;

@Configuration
public class DelegatingResource4jConfiguration extends Resource4jConfigurationSupport implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Bean
	public ResourceObjectFactory fileFactory() {
		MappingResourceObjectFactory factory = new MappingResourceObjectFactory();
		Map<String, ResourceObjectFactory> mappings = new LinkedHashMap<>();
		mappings.put(".+\\.properties$", new ClasspathResourceObjectFactory());
		SpringResourceObjectFactory springResourceFactory = new SpringResourceObjectFactory();
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
