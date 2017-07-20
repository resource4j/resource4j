package com.github.resource4j.thymeleaf3;

import com.github.resource4j.spring.config.Resource4jAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ Resource4jAutoConfiguration.class, Thymeleaf3ResourceConfiguration.class })
public class Resource4jMessageResolverTest {
	
	@Autowired
	private ITemplateEngine engine;
	
	@Test
	public void testRenderRussianPageSubstitutesRussianString() {
		String content = engine.process("example/pages/page.html", new Context(new Locale("ru", "RU")));
		System.out.println(content);
		assertTrue(content.contains("\u041F\u0440\u0438\u043C\u0435\u0440 \u0441\u0442\u0440\u0430\u043D\u0438\u0446\u044B"));
	}
	
	@Test
	public void testRenderRussianPageUsesDefaultBundle() {
		String content = engine.process("example/pages/page.html", new Context(new Locale("ru", "RU")));
		assertTrue(content.contains("Latin"));
	}
}
