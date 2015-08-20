package com.github.resource4j.spring;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import example.i18n.Greeting;
import example.i18n.Messages;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/testContext.xml")
public class ResourceValueBeanPostProcessorIT implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Test
	public void testSimpleTypeValueInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		Messages bean = new Messages();
		beanFactory.initializeBean(bean, "messages");
		assertEquals("Hello, {}!", bean.getWelcome());
	}
	
	@Test
	public void testAutowiredBundleClassInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		Greeting bean = new Greeting();
		beanFactory.initializeBean(bean, "greeting");
		assertEquals("Hello, {}!", bean.getWelcome());
	}
	
	@Test
	public void testAutowiredBundleNameInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		Greeting bean = new Greeting();
		beanFactory.initializeBean(bean, "greeting");
		assertEquals("Dear Customer", bean.getName());
	}
	
	@Test
	public void testMandatoryValueInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		Messages bean = new Messages();
		beanFactory.initializeBean(bean, "messages");
		assertEquals("Web", bean.getApplication().asIs());
	}
	
	@Test
	public void testReferenceInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		Messages bean = new Messages();
		beanFactory.initializeBean(bean, "messages");
		assertEquals("See you!", bean.getGoodbyeValue().fetch(in(Locale.US)).asIs());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
