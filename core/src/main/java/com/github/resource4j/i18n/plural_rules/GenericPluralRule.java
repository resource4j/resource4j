package com.github.resource4j.i18n.plural_rules;

import java.util.List;
import java.util.Locale;

public class GenericPluralRule implements PluralRule {

    private Locale locale;

    private List<PluralCase> cases;

    public GenericPluralRule(Locale locale,
                             List<PluralCase> cases) {
        this.locale = locale;
        this.cases = cases;
    }

    @Override
    public Locale language() {
        return locale;
    }

    @Override
    public PluralCategory pluralize(int number) {
        for (PluralCase pluralCase : cases) {
            if (pluralCase.includes(number))
                return pluralCase.category();
        }
        return PluralCategory.many;
    }

}
