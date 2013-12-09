package com.github.resource4j.examples.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages="com.github.resource4j.examples.web.controller")
@EnableWebMvc
public class WebConfiguration {

}
