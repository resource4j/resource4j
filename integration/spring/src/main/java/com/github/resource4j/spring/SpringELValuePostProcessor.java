package com.github.resource4j.spring;

import com.github.resource4j.ResourceException;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;
import com.github.resource4j.resources.processors.ResourceValuePostProcessor;
import com.github.resource4j.resources.processors.ValuePostProcessingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static com.github.resource4j.converters.TypeConverter.convert;
import static java.util.Collections.emptyMap;

public class SpringELValuePostProcessor implements ResourceValuePostProcessor, BeanFactoryAware {

    private SpelExpressionParser parser = new SpelExpressionParser();

    private BeanFactoryResolver beanFactoryResolver;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactoryResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public String process(String val, ResourceResolutionContext context, ResourceResolver resolver) throws ResourceException {
        String value = val;
        if (value.startsWith("~")) {
            return value.substring(1);
        }
        if (!value.startsWith("#")) {
            return value;
        }
        try {
            value = value.substring(1);
            Expression expression = parser.parseExpression(value);
            StandardEvaluationContext evalContext = new StandardEvaluationContext();
            evalContext.setBeanResolver((ctx, beanName) -> {
                String value1 = convert(resolver.get(beanName, emptyMap()), String.class);
                if (value1 != null) {
                    return value1;
                }
                return beanFactoryResolver != null
                        ? beanFactoryResolver.resolve(ctx, beanName)
                        : null;
            });
            Object resolvedValue = expression.getValue(evalContext, String.class);
            return String.valueOf(resolvedValue);
        } catch (EvaluationException e) {
            String expr = e.getExpressionString();
            throw new ValuePostProcessingException(expr != null ? expr : value);
        }
    }
}
