package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.ldml.LDMLRuleLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public final class PluralRules {

    private static final Logger LOG = LoggerFactory.getLogger(PluralRules.class);

    private static final Map<Locale, PluralRule> RULES_INDEXED_BY_LOCALE;

    static {
        Map<Locale, PluralRule> rules;
        try {
            rules = LDMLRuleLoader.load();
        } catch (NoClassDefFoundError e) {
            LOG.warn("Pluralization rules not loaded: java.xml module is not available");
            rules = Collections.emptyMap();
        }
        RULES_INDEXED_BY_LOCALE = rules;
    }

    public Map<Locale, PluralRule> get() {
        return RULES_INDEXED_BY_LOCALE;
    }

}
