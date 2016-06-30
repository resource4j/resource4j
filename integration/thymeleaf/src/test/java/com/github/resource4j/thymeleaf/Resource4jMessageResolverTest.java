package com.github.resource4j.thymeleaf;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ ThymeleafResourceConfiguration.class })
public class Resource4jMessageResolverTest {
	
	@Autowired
	private TemplateEngine engine;
	
	@Test
	public void testRenderRussianPageSubstitutesRussianString() {
		String content = engine.process("example/pages/page", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("\u041F\u0440\u0438\u043C\u0435\u0440 \u0441\u0442\u0440\u0430\u043D\u0438\u0446\u044B"));
	}
	
	@Test
	public void testRenderRussianPageUsesDefaultBundle() {
		String content = engine.process("example/pages/page", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("Latin"));
	}
}
