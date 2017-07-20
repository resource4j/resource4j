package com.github.resource4j.i18n;

import java.util.List;
import java.util.Locale;

public class GenericPluralizationRule implements PluralizationRule {

    private Locale locale;

    private List<PluralizationCase> cases;

    public GenericPluralizationRule(Locale locale,
                                    List<PluralizationCase> cases) {
        this.locale = locale;
        this.cases = cases;
    }

    @Override
    public Locale language() {
        return locale;
    }

    @Override
    public PluralCategory pluralize(int number) {
        for (PluralizationCase pluralizationCase : cases) {
            if (pluralizationCase.includes(number))
                return pluralizationCase.category();
        }
        return PluralCategory.many;
    }

}
