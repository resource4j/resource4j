package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.converters.TypeConverter;
import com.github.resource4j.i18n.PluralizationRule;
import com.github.resource4j.i18n.PluralizationRuleFactory;
import com.github.resource4j.resources.context.LocaleResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.util.Locale;
import java.util.Map;

public class NumberPluralizationStrategy implements PropertyResolver {

    private Map<Locale, PluralizationRule> ruleIndex = new PluralizationRuleFactory().get();

    @Override
    public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
        if (!"pluralize".equals(property) || value == null) {
            return null;
        }
        int number = TypeConverter.convert(value, Integer.class);
        // detect locale:
        Locale locale = Locale.getDefault();
        for (ResourceResolutionComponent component : context.components()) {
            if (component instanceof LocaleResolutionComponent) {
                locale = ((LocaleResolutionComponent) component).locale();
            }
        }
        PluralizationRule rule = ruleIndex.get(locale);
        if (rule == null) {
            rule = ruleIndex.get(Locale.ENGLISH);
        }
        return rule.pluralize(number);
    }

}
