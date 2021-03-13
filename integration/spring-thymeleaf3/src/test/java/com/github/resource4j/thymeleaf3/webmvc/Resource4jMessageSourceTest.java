package com.github.resource4j.thymeleaf3.webmvc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Configuration
@EnableAutoConfiguration
@ContextConfiguration(classes={ Resource4jMessageSourceTest.class })
public class Resource4jMessageSourceTest {
	
	@Autowired
	private ITemplateEngine engine;
	
	@Test
	public void testRenderRussianPageSubstitutesRussianString() {
		String content = engine.process("example/pages/page", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("\u041F\u0440\u0438\u043C\u0435\u0440 \u0441\u0442\u0440\u0430\u043D\u0438\u0446\u044B"));
	}
	
	@Test
	public void testRenderRussianPageUsesDefaultBundle() {
		String content = engine.process("example/pages/page.html", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("Latin"));
	}
}
