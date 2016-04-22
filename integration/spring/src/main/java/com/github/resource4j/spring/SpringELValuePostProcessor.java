package com.github.resource4j.spring;

import com.github.resource4j.ResourceException;
import com.github.resource4j.resources.processors.ResourceResolver;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;
import com.github.resource4j.resources.processors.ValuePostProcessingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringELValuePostProcessor implements ResourceValuePostProcessor, BeanFactoryAware {

    private SpelExpressionParser parser = new SpelExpressionParser();

    private BeanFactoryResolver beanFactoryResolver;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactoryResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public String process(ResourceResolver resolver, String value) throws ResourceException {
        try {
            Expression expression = parser.parseExpression(value);
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setBeanResolver((evaluationContext, beanName) -> {
                String value1 = resolver.get(beanName);
                if (value1 != null) {
                    return value1;
                }
                return beanFactoryResolver != null
                        ? beanFactoryResolver.resolve(context, beanName)
                        : null;
            });
            return String.valueOf(expression.getValue(context));
        } catch (EvaluationException e) {
            String expr = e.getExpressionString();
            throw new ValuePostProcessingException(expr != null ? expr : value);
        }
    }
}
