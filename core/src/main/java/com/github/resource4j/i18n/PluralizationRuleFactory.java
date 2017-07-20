package com.github.resource4j.i18n;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
        try {
            Map<Locale, PluralizationRule> index = new HashMap<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getClass().getResourceAsStream("/cldr/common/supplemental/plurals.xml"));
            NodeList ruleList = document.getElementsByTagName("pluralRules");
            for (int i = 0; i < ruleList.getLength(); i++) {
                Element rules = (Element) ruleList.item(i);
                String[] locales = rules.getAttribute("locales").split(" ");
                NodeList ruleDefs = rules.getElementsByTagName("pluralRule");
                List<PluralizationCase> cases = new ArrayList<>();
                for (int j = 0; j < ruleDefs.getLength(); j++) {
                    Element rule = (Element) ruleDefs.item(j);
                    String count = rule.getAttribute("count");
                    String text = rule.getTextContent();
                    cases.add(aCase(PredicateParser.parse(text), PluralCategory.valueOf(count)));
                }
                for (String locale : locales) {
                    PluralizationRuleBuilder ruleBuilder = aRuleFor(Locale.forLanguageTag(locale));
                    for (PluralizationCase pluralizationCase : cases) {
                        ruleBuilder.add(pluralizationCase);
                    }
                    PluralizationRule rule = ruleBuilder.build();
                    index.put(rule.language(), rule);
                }
            }
            return index;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load pluralization rules", e);
        }
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
