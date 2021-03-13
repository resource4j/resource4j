package com.github.resource4j.demo;

import com.github.resource4j.spring.config.EnableResources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(LocalizedWebMvcConfiguration.class)
@EnableResources
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class
})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
