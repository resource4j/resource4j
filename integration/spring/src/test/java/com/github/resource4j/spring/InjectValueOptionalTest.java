package com.github.resource4j.spring;

import com.github.resource4j.spring.annotations.InjectBundle;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class InjectValueOptionalTest implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@InjectBundle("example.config.application")
	public static class StringValueModel {
		
		@InjectValue("mail_user")
		private Optional<String> value;
		
		public Optional<String> getValue() {
			return value;
		}
		public void setValue(Optional<String> value) {
			this.value = value;
		}
	}
	
	@InjectBundle("example.config.application")
	public static class IntValueModel {
		@InjectValue("mail_password")
		private Optional<Integer> code;
		public Optional<Integer> getCode() {
			return code;
		}
		public void setCode(Optional<Integer> code) {
			this.code = code;
		}
	}
	
	@InjectBundle("example.config.application")
	public static class NumberValueModel {
		@InjectValue("mail_password")
		private Optional<? extends Number> code;
		public Optional<? extends Number> getCode() {
			return code;
		}
		public void setCode(Optional<Integer> code) {
			this.code = code;
		}
	}
	
	@Test
	public void testInjectStringOptional() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		StringValueModel bean = new StringValueModel();
		beanFactory.initializeBean(bean, "model");
		assertNotNull(bean.getValue());
		assertTrue(bean.getValue().isPresent());
		assertEquals("somebody", bean.getValue().get());		
	}
	
	@Test
	public void testInjectIntOptional() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		IntValueModel bean = new IntValueModel();
		beanFactory.initializeBean(bean, "model");
		assertNotNull(bean.getCode());
		assertTrue(bean.getCode().isPresent());
		assertEquals(12345, bean.getCode().get().intValue());		
	}
	
	@Test
	public void testInjectNumberWildcardTypeOptional() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		NumberValueModel bean = new NumberValueModel();
		beanFactory.initializeBean(bean, "model");
		assertNotNull(bean.getCode());
		assertTrue(bean.getCode().isPresent());
		assertEquals(12345, bean.getCode().get().intValue());		
	}
}
