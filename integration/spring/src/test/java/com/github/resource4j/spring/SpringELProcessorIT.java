package com.github.resource4j.spring;

import com.github.resource4j.spring.test.TestConfiguration;
import example.el.User;
import example.el.Warning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
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
