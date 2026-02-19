package com.github.resource4j.thymeleaf3.autoconfigure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Configuration
@EnableAutoConfiguration
@ContextConfiguration(classes = {ContextAwareSpringTemplateEngineAutoConfigurationTest.class})
public class ContextAwareSpringTemplateEngineAutoConfigurationTest {

    @Autowired
    private ISpringTemplateEngine springTemplateEngine;

    @Autowired
    private ITemplateEngine templateEngine;

    @Test
    public void testSpringTemplateEngineIsContextAware() {
        assertInstanceOf(ContextAwareSpringTemplateEngine.class, springTemplateEngine);
    }

    @Test
    public void testTemplateEngineIsContextAware() {
        assertInstanceOf(ContextAwareSpringTemplateEngine.class, templateEngine);
    }

    @Test
    public void testResolvesLocaleSpecificTemplate() {
        String content = springTemplateEngine.process("example/pages/page", new Context(new Locale("de", "DE")));
        assertTrue(content.contains("Deutsch"));
    }

    @Test
    public void testResolvesMessageFromBundle() {
        String content = springTemplateEngine.process("example/pages/page", new Context(new Locale("ru", "RU")));
        assertTrue(content.contains("\u041F\u0440\u0438\u043C\u0435\u0440 \u0441\u0442\u0440\u0430\u043D\u0438\u0446\u044B"));
    }

}
