package com.github.resource4j.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.legal.EULA;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/testContext.xml")
public class ResourceFileAutowiringIT implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Test
	public void testByteContentInjectedFromResourceFileWhenAutowiring() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		EULA bean = new EULA();
		beanFactory.initializeBean(bean, "eula");
		assertEquals(1612, bean.getLogo().length);
	}
}
