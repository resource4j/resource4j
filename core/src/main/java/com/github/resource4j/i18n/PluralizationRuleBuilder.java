package com.github.resource4j.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PluralizationRuleBuilder {

    private final Locale locale;
    private List<PluralizationCase> cases = new ArrayList<>();

    public PluralizationRuleBuilder(Locale locale) {
        this.locale = locale;
    }

    public static PluralizationRuleBuilder aRuleFor(Locale locale) {
        return new PluralizationRuleBuilder(locale);
    }

    public PluralizationRuleBuilder add(PluralizationCase aCase) {
        this.cases.add(aCase);
        return this;
    }

    public PluralizationRule build() {
        return new GenericPluralizationRule(locale, cases);
    }


}
