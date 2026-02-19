package com.github.resource4j.i18n.plural_rules;

import com.github.resource4j.i18n.plural_rules.ldml.LDMLRuleLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.i18n.plural_rules.PluralCategory.*;
import static org.junit.jupiter.api.Assertions.*;

public class PluralRulesTest {

    private static Map<Locale, PluralRule> rules;

    @BeforeAll
    static void loadRules() {
        rules = LDMLRuleLoader.load();
    }

    @Test
    public void shouldLoadNonEmptyRuleMap() {
        assertNotNull(rules);
        assertFalse(rules.isEmpty(), "Plural rules map should not be empty");
    }

    @Test
    public void shouldContainEnglish() {
        assertNotNull(rules.get(Locale.ENGLISH));
    }

    @Test
    public void shouldContainRussian() {
        assertNotNull(rules.get(Locale.forLanguageTag("ru")));
    }

    @Test
    public void shouldContainArabic() {
        assertNotNull(rules.get(Locale.forLanguageTag("ar")));
    }

    @Test
    public void shouldContainJapanese() {
        assertNotNull(rules.get(Locale.JAPANESE));
    }

    @Test
    public void shouldContainGerman() {
        assertNotNull(rules.get(Locale.GERMAN));
    }

    @Test
    public void shouldContainFrench() {
        assertNotNull(rules.get(Locale.FRENCH));
    }

    @Nested
    class EnglishRules {
        private final PluralRule rule = rules.get(Locale.ENGLISH);

        @Test
        public void oneForSingular() {
            assertEquals(one, rule.pluralize(1));
        }

        @Test
        public void otherForZero() {
            assertEquals(other, rule.pluralize(0));
        }

        @Test
        public void otherForPlural() {
            assertEquals(other, rule.pluralize(2));
            assertEquals(other, rule.pluralize(5));
            assertEquals(other, rule.pluralize(10));
            assertEquals(other, rule.pluralize(100));
        }
    }

    @Nested
    class RussianRules {
        private final PluralRule rule = rules.get(Locale.forLanguageTag("ru"));

        @Test
        public void oneForSingular() {
            assertEquals(one, rule.pluralize(1));
            assertEquals(one, rule.pluralize(21));
            assertEquals(one, rule.pluralize(31));
            assertEquals(one, rule.pluralize(101));
        }

        @Test
        public void fewForTwoToFour() {
            assertEquals(few, rule.pluralize(2));
            assertEquals(few, rule.pluralize(3));
            assertEquals(few, rule.pluralize(4));
            assertEquals(few, rule.pluralize(22));
            assertEquals(few, rule.pluralize(34));
        }

        @Test
        public void manyForFiveToTwenty() {
            assertEquals(many, rule.pluralize(0));
            assertEquals(many, rule.pluralize(5));
            assertEquals(many, rule.pluralize(11));
            assertEquals(many, rule.pluralize(12));
            assertEquals(many, rule.pluralize(14));
            assertEquals(many, rule.pluralize(19));
            assertEquals(many, rule.pluralize(100));
        }

        @Test
        public void manyForTeens() {
            assertEquals(many, rule.pluralize(11));
            assertEquals(many, rule.pluralize(12));
            assertEquals(many, rule.pluralize(13));
            assertEquals(many, rule.pluralize(14));
            assertEquals(many, rule.pluralize(111));
            assertEquals(many, rule.pluralize(112));
        }
    }

    @Nested
    class ArabicRules {
        private final PluralRule rule = rules.get(Locale.forLanguageTag("ar"));

        @Test
        public void zeroForZero() {
            assertEquals(zero, rule.pluralize(0));
        }

        @Test
        public void oneForOne() {
            assertEquals(one, rule.pluralize(1));
        }

        @Test
        public void twoForTwo() {
            assertEquals(two, rule.pluralize(2));
        }

        @Test
        public void fewForThreeToTen() {
            assertEquals(few, rule.pluralize(3));
            assertEquals(few, rule.pluralize(7));
            assertEquals(few, rule.pluralize(10));
            assertEquals(few, rule.pluralize(103));
        }

        @Test
        public void manyForElevenToNinetyNine() {
            assertEquals(many, rule.pluralize(11));
            assertEquals(many, rule.pluralize(26));
            assertEquals(many, rule.pluralize(99));
            assertEquals(many, rule.pluralize(111));
        }

        @Test
        public void otherForHundreds() {
            assertEquals(other, rule.pluralize(100));
            assertEquals(other, rule.pluralize(200));
        }
    }

    @Nested
    class FrenchRules {
        private final PluralRule rule = rules.get(Locale.FRENCH);

        @Test
        public void oneForZeroAndOne() {
            // i = 0,1
            assertEquals(one, rule.pluralize(0));
            assertEquals(one, rule.pluralize(1));
        }

        @Test
        public void otherForSmallNumbers() {
            assertEquals(other, rule.pluralize(2));
            assertEquals(other, rule.pluralize(5));
            assertEquals(other, rule.pluralize(17));
            assertEquals(other, rule.pluralize(100));
            assertEquals(other, rule.pluralize(1000));
        }

        @Test
        public void otherForLargeNumbers() {
            // Rules with 'e' operand (compact decimal exponent) are skipped
            // since pluralize(int) has no compact formatting context.
            // Millions fall through to "other" like any number >= 2.
            assertEquals(other, rule.pluralize(1000000));
            assertEquals(other, rule.pluralize(2000000));
            assertEquals(other, rule.pluralize(1000001));
            assertEquals(other, rule.pluralize(1500000));
        }
    }

    @Nested
    class JapaneseRules {
        private final PluralRule rule = rules.get(Locale.JAPANESE);

        @Test
        public void otherForAll() {
            assertEquals(other, rule.pluralize(0));
            assertEquals(other, rule.pluralize(1));
            assertEquals(other, rule.pluralize(2));
            assertEquals(other, rule.pluralize(100));
        }
    }

}
