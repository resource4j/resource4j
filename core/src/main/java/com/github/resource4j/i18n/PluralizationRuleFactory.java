package com.github.resource4j.i18n;

import com.github.resource4j.i18n.plural_rules.PredicateParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static com.github.resource4j.i18n.GenericPluralizationCase.aCase;
import static com.github.resource4j.i18n.PluralizationRuleBuilder.aRuleFor;

public class PluralizationRuleFactory implements Supplier<Map<Locale, PluralizationRule>> {

    private Map<Locale, PluralizationRule> ruleIndex;

    /**
     * TODO: add Unicode license text to project
     * @return index of pluralization rules
     */
    private Map<Locale, PluralizationRule> createRules() {
        Map<Locale, PluralizationRule> map = new HashMap<>();
        PluralizationRule rule = PluralizationRuleBuilder
                .aRuleFor(Locale.ENGLISH)
                .add(aCase(number -> number.toString().equals("1"), PluralCategory.one))
                .add(aCase(number -> true, PluralCategory.many))
                .build();
        map.put(Locale.ENGLISH, rule);
        return map;
    }

    @Override
    public Map<Locale, PluralizationRule> get() {
        if (ruleIndex == null) {
            synchronized (this) {
                if (ruleIndex == null) {
                    ruleIndex = createRules();
                }
            }
        }
        return ruleIndex;
    }
}
