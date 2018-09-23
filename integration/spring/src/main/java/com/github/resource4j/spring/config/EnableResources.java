package com.github.resource4j.spring.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(Resource4jAutoConfiguration.class)
public @interface EnableResources {

}
