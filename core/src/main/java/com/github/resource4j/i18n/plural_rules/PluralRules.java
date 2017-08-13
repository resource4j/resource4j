package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.ldml.LDMLRuleLoader;

import java.util.Locale;
import java.util.Map;

public final class PluralRules {

    private static final Map<Locale, PluralRule> RULES_INDEXED_BY_LOCALE;

    static {
        RULES_INDEXED_BY_LOCALE = LDMLRuleLoader.load();
    }

    public Map<Locale, PluralRule> get() {
        return RULES_INDEXED_BY_LOCALE;
    }

}
