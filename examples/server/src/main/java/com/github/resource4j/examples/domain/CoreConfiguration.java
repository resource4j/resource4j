package com.github.resource4j.examples.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.github.resource4j.resources.DefaultResources;
import com.github.resource4j.resources.Resources;

@Configuration
@ComponentScan(basePackages="com.github.resource4j.examples.domain.service")
public class CoreConfiguration {

	@Bean
	public Resources resources() {
		return new DefaultResources();
	}
	
}
