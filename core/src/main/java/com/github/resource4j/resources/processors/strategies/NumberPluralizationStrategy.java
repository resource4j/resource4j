package com.github.resource4j.resources.processors.strategies;

import com.github.resource4j.converters.TypeConverter;
import com.github.resource4j.i18n.plural_rules.PluralRule;
import com.github.resource4j.i18n.plural_rules.PluralRules;
import com.github.resource4j.resources.context.LocaleResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionComponent;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.resources.processors.ResourceResolver;

import java.util.Locale;
import java.util.Map;

public class NumberPluralizationStrategy implements PropertyResolver {

    private Map<Locale, PluralRule> ruleIndex = new PluralRules().get();

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
        PluralRule rule = ruleIndex.get(locale);
        if (rule == null) {
            rule = ruleIndex.get(Locale.ENGLISH);
        }
        return rule.pluralize(number);
    }

}
