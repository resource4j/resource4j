package com.github.resource4j.i18n.plural_rules;

import java.util.function.Predicate;

public class GenericPluralCase implements PluralCase {

    private final PluralCategory category;
    private final Predicate<Number> predicate;

    public static GenericPluralCase aCase(Predicate<Number> predicate,
                                          PluralCategory category) {
        return new GenericPluralCase(category, predicate);
    }

    public GenericPluralCase(PluralCategory category,
                             Predicate<Number> predicate) {
        this.category = category;
        this.predicate = predicate;
    }

    @Override
    public PluralCategory category() {
        return this.category;
    }

    @Override
    public boolean includes(Number number) {
        return this.predicate.test(number);
    }
}
