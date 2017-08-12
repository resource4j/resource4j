package com.github.resource4j.i18n;

import com.github.resource4j.i18n.plural_rules.PredicateParser;
import com.github.resource4j.i18n.plural_rules.XMLRuleDefinition;
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
        Map<Locale, PluralizationRule> map = XMLRuleDefinition.load();
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
