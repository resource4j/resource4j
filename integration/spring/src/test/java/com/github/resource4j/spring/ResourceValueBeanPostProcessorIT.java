package com.github.resource4j.spring;

import com.github.resource4j.spring.test.TestConfiguration;
import example.i18n.Greeting;
import example.i18n.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ResourceValueBeanPostProcessorIT implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@BeforeEach
	public void setLocale() {
		LocaleContextHolder.setLocale(Locale.US);
	}

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
