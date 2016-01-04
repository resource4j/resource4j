package com.github.resource4j.spring.config;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.resource4j.objects.providers.ClasspathResourceObjectProvider;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.spring.SpringResourceObjectProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.resource4j.objects.providers.MappingResourceObjectProvider;
import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;

@Configuration
public class DelegatingResource4jConfiguration extends Resource4jConfigurationSupport implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Bean
	public ResourceObjectProvider fileFactory() {
		MappingResourceObjectProvider factory = new MappingResourceObjectProvider();
		Map<String, ResourceObjectProvider> mappings = new LinkedHashMap<>();
		mappings.put(".+\\.properties$", new ClasspathResourceObjectProvider());
		SpringResourceObjectProvider springResourceFactory = new SpringResourceObjectProvider();
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
