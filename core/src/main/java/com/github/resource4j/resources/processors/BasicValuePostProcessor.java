package com.github.resource4j.resources.processors;

import com.github.resource4j.ResourceException;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.parser.BasicStateMachine;
import com.github.resource4j.resources.processors.strategies.*;

import java.util.ArrayList;
import java.util.List;

public class BasicValuePostProcessor implements ResourceValuePostProcessor {

    public static BasicValuePostProcessor macroSubstitution() {
        return new BasicValuePostProcessor();
    }

    private List<PropertyResolver> resolutionStrategies;

    public BasicValuePostProcessor() {
        this.resolutionStrategies = new ArrayList<>();
        this.resolutionStrategies.add(new DateFormatStrategy());
        this.resolutionStrategies.add(new StringCaseStrategy());
        this.resolutionStrategies.add(new NumberPluralizationStrategy());
        this.resolutionStrategies.add(new ReflectionStrategy());
    }

    @Override
    public String process(String value, ResourceResolutionContext context, ResourceResolver resolver) throws ResourceException {
        BasicStateMachine machine = BasicStateMachine.start((key, property, params) -> {
            Object val = resolver.get(key, params);
            if (property != null) {
                for (PropertyResolver strategy : resolutionStrategies) {
                    Object resolution = strategy.resolve(val, property, context, resolver);
                    if (resolution != null) {
                        val = resolution;
                        break;
                    }
                }
            }
            return val;
        });
        char[] chars = value.toCharArray();
        for (char c : chars) {
            machine.onReceived(c);
        }
        boolean partialResult = !machine.onComplete();
        String result = machine.toString();
        if (partialResult) {
            throw new ValuePostProcessingException(result);
        }
        return result;
    }

}
