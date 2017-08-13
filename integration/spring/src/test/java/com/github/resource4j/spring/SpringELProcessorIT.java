package com.github.resource4j.spring;

import com.github.resource4j.spring.test.TestConfiguration;
import example.el.User;
import example.el.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, SpringELProcessorIT.Config.class })
public class SpringELProcessorIT  implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testByteContentInjectedFromResourceFileWhenAutowiring() {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        Warning bean = new Warning();
        beanFactory.initializeBean(bean, "warning");
        assertEquals("Hello, John!", bean.getMessage());
        assertEquals("#", bean.getEscaped());
    }

    public static class Config {

        @Bean(name = "user")
        public User getUser() {
            return new User();
        }

    }

}
