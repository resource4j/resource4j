package com.github.resource4j.i18n.plural_rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PluralRuleBuilder {

    private final Locale locale;
    private List<PluralCase> cases = new ArrayList<>();

    public PluralRuleBuilder(Locale locale) {
        this.locale = locale;
    }

    public static PluralRuleBuilder aRuleFor(Locale locale) {
        return new PluralRuleBuilder(locale);
    }

    public PluralRuleBuilder add(PluralCase aCase) {
        this.cases.add(aCase);
        return this;
    }

    public PluralRule build() {
        return new GenericPluralRule(locale, cases);
    }


}
