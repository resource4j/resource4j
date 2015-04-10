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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/testContext.xml")
public class ResourceValueBeanPostProcessorIT implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Test
	public void testSimpleTypeValueInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		ExampleBean bean = new ExampleBean();
		beanFactory.initializeBean(bean, "exampleBean");
		assertEquals("Success", bean.getValue());
	}
	
	@Test
	public void testAutowiredBundleClassInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		AutowireBundleExample bean = new AutowireBundleExample();
		beanFactory.initializeBean(bean, "autowireBundle");
		assertEquals("Success", bean.getValue());
	}
	
	@Test
	public void testAutowiredBundleNameInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		AutowireBundleExample bean = new AutowireBundleExample();
		beanFactory.initializeBean(bean, "autowireBundle");
		assertEquals("New Zealand", bean.getCountry());
	}
	
	@Test
	public void testMandatoryValueInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		ExampleBean bean = new ExampleBean();
		beanFactory.initializeBean(bean, "exampleBean");
		assertEquals("Web Title", bean.getTitle().asIs());
	}
	
	@Test
	public void testReferenceInjection() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		ExampleBean bean = new ExampleBean();
		beanFactory.initializeBean(bean, "exampleBean");
		assertEquals("Correct", bean.getSecondValue().fetch(in(Locale.US)).asIs());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
