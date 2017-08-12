package com.github.resource4j.i18n.plural_rules;

import java.util.ArrayList;
import java.util.List;

public class RangeList {

    private final static NumberComparator C = NumberComparator.compareNumbers();

    private final List<NumberSet> subsets = new ArrayList<>();

    public RangeList(Number min, Number max) {
        this.subsets.add(number -> C.compare(min, number) + C.compare(number, max) < 0);
    }

    public RangeList(Number value) {
        this.subsets.add(number -> C.compare(value, number) == 0);
    }

    public boolean contains(Number number) {
        for (NumberSet set : subsets) {
            if (set.contains(number)) return true;
        }
        return false;
    }

    public RangeList add(RangeList list) {
        this.subsets.addAll(list.subsets);
        return this;
    }
}
