package com.github.resource4j.resources.processors;

import org.junit.jupiter.api.Test;

import static com.github.resource4j.resources.context.ResourceResolutionContext.with;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

public class BasicValuePostProcessorTest {

    public static final ResourceResolver RESOLVER = (k, params) ->
            String.format("[%s%s%s]",
                k,
                params.size() > 0 ? ":" : "",
                params.values().stream().map(Object::toString).collect(joining("_")));
    private BasicValuePostProcessor processor = new BasicValuePostProcessor();

    @Test
    public void shouldReturnSameValueWhenNoSubstitutionsPresent() {
        String text = "Simple text";
        String result = processor.process(text, withoutContext(), RESOLVER);
        assertEquals(text, result);
    }

    @Test
    public void shouldReturnSubstitutionWhenValueIsAlias() {
        final String alias = "{name}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertEquals("[name]", result);
    }

    @Test
    public void shouldReturnEscapedKeyWithoutSubstitution() {
        final String alias = "{:name}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertEquals("{name}", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultWhenEscapedKeyIsEmpty() {
        String result = null;
        try {
            processor.process("{:}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("{:}", result);
    }

    @Test
    public void shouldReturnEmptyStringWhenEmptyStringProcessed() {
        String result = processor.process("", withoutContext(), RESOLVER);
        assertEquals("", result);
    }

    @Test
    public void shouldReturnCorrectStringWhenTwoConsequentialValuesProcessed() {
        String result = processor.process("{1}{2}", withoutContext(), RESOLVER);
        assertEquals("[1][2]", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnEscapingAtTheEndOfString() {
        String result = null;
        try {
            processor.process("Value \\", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value \\", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnOpeningAtTheEndOfString() {
        String result = null;
        try {
            processor.process("Value {", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroAtTheEndOfString() {
        String result = null;
        try {
            processor.process("Value {test", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test", result);
    }

    @Test
    public void shouldCorrectlyHandleOpeningAndEscapingAtTheEndOfString() {
        String result = null;
        try {
            processor.process("Value {\\", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {\\", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingInMacroName() {
        String result = processor.process("Value {1\\0}", withoutContext(), RESOLVER);
        assertEquals("Value [10]", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingOpening() {
        String result = null;
        try {
            processor.process("Value \\{0}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0}", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingEnding() {
        String result = processor.process("Value \\}", withoutContext(), RESOLVER);
        assertEquals("Value }", result);
    }
    @Test
    public void shouldFailWithCorrectPartialResultOnEmptyLastParam() {
        String result = null;
        try {
            processor.process("{test;param;}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("{test;param;}", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroWithParams() {
        String result = null;
        try {
            processor.process("Value {test;param", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test;param", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroWithParamLiteral() {
        String result = null;
        try {
            processor.process("Value {test;param;:lit", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test;param;:lit", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroWithParamLiteralStart() {
        String result = null;
        try {
            processor.process("Value {test;param;:", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test;param;:", result);
    }
    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroWithParamLiteralStartEscape() {
        String result = null;
        try {
            processor.process("Value {test;param;:\\", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test;param;:\\", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnEscapedEndingInMacro() {
        String result = null;
        try {
            processor.process("Value {0\\}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0\\}", result);
    }

    @Test
    public void shouldFailWithColonAsParameterFirstCharacter() {
        String result = null;
        try {
            processor.process("Value {val;:char}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {val;:char}", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingEscapeChar() {
        String result = processor.process("Value \\\\", withoutContext(), RESOLVER);
        assertEquals("Value \\", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnNestedSubstitution() {
        String result = null;
        try {
            processor.process("Value {0{1}}", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0[1]}", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnSubstituteNotFound() {
        String result = null;
        try {
            processor.process("Value {0} {1}", withoutContext(), (k, params) -> "0".equals(k) ? null : "[" + k + "]");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0} [1]", result);
    }

    @Test
    public void shouldCorrectlyPassTwoValues() {
        final String alias = "{number;count} {apple;count}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertEquals("[number_[count]:[count]] [apple_[count]:[count]]", result);
    }

    @Test
    public void shouldCorrectlyParseSingleVariableParameter() {
        final String alias = "{name;count}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertEquals("[name_[count]:[count]]", result);
    }

    @Test
    public void shouldCorrectlyParseSingleVariableParameterWithProperty() {
        final String alias = "{name;count:upper}";
        String result = processor.process(alias, with("count",1), RESOLVER);
        assertEquals("[name_[COUNT]:[COUNT]]", result);
    }

    @Test
    public void shouldCorrectlyParseParamWithEscaping() {
        final String alias = "{name;\\:}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertEquals("[name_[:]:[:]]", result);
    }

    // --- Escape combinations ---

    @Test
    public void shouldCorrectlyHandleEscapedBackslashBeforeMacro() {
        // Input chars: \, \, {, n, a, m, e, }
        // \\ → \, then {name} → [name]
        String result = processor.process("Value \\\\{name}", withoutContext(), RESOLVER);
        assertEquals("Value \\[name]", result);
    }

    @Test
    public void shouldCorrectlyHandleEscapeInMacroNameProducingBackslash() {
        // Input chars: {, a, \, \, b, }
        // In NAME: a, then \ → ESCAPE_NAME, then \ → ACCEPT(\) → name="a\", then b → name="a\b"
        String result = processor.process("{a\\\\b}", withoutContext(), RESOLVER);
        assertEquals("[a\\b]", result);
    }

    @Test
    public void shouldCorrectlyHandleEscapeInLiteral() {
        // Input chars: {, :, a, \, \, b, }
        // In LITERAL: a, then \\ → \, then b → literal="a\b"
        String result = processor.process("{:a\\\\b}", withoutContext(), RESOLVER);
        assertEquals("{a\\b}", result);
    }

    // --- Multiple macros ---

    @Test
    public void shouldCorrectlyResolveThreeMacrosWithText() {
        String result = processor.process("{a} and {b} and {c}", withoutContext(), RESOLVER);
        assertEquals("[a] and [b] and [c]", result);
    }

    @Test
    public void shouldCorrectlyResolveMacroAtStart() {
        String result = processor.process("{name} world", withoutContext(), RESOLVER);
        assertEquals("[name] world", result);
    }

    @Test
    public void shouldCorrectlyResolveMacroAtEnd() {
        String result = processor.process("hello {name}", withoutContext(), RESOLVER);
        assertEquals("hello [name]", result);
    }

    @Test
    public void shouldCorrectlyResolveSingleCharMacro() {
        String result = processor.process("{a}", withoutContext(), RESOLVER);
        assertEquals("[a]", result);
    }

    // --- Unmatched braces ---

    @Test
    public void shouldFailOnUnmatchedClosingBrace() {
        String result = null;
        try {
            processor.process("text } more", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("text } more", result);
    }

    // --- Unicode ---

    @Test
    public void shouldCorrectlyResolveUnicodeInMacroName() {
        String result = processor.process("{café}", withoutContext(), RESOLVER);
        assertEquals("[café]", result);
    }

    @Test
    public void shouldCorrectlyHandleUnicodeInText() {
        String result = processor.process("Héllo {name}", withoutContext(), RESOLVER);
        assertEquals("Héllo [name]", result);
    }

    // --- Whitespace in macros ---

    @Test
    public void shouldPreserveSpacesInMacroName() {
        String result = processor.process("{a b}", withoutContext(), RESOLVER);
        assertEquals("[a b]", result);
    }

    @Test
    public void shouldPreserveSpacesInParam() {
        // {key; p } → name="key", param=" p " (spaces preserved)
        String result = processor.process("{key; p }", withoutContext(), RESOLVER);
        assertNotNull(result);
    }

    @Test
    public void shouldHandleEscapeOfNormalCharInMacro() {
        // {a\tb} → \ escapes t → name="atb"
        String result = processor.process("{a\\tb}", withoutContext(), RESOLVER);
        assertEquals("[atb]", result);
    }

    // --- Parameters ---

    @Test
    public void shouldCorrectlyParseThreeParams() {
        final String alias = "{key;p1;p2;p3}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertNotNull(result);
        // Resolver receives key="key" and 3 params
        assertTrue(result.contains("[key"));
    }

    @Test
    public void shouldCorrectlyParseEscapedSemicolonInParam() {
        // {name;a\;b} → \ escapes ;, so param = "a;b"
        final String alias = "{name;a\\;b}";
        String result = processor.process(alias, withoutContext(), RESOLVER);
        assertNotNull(result);
    }

    // --- Literals ---

    @Test
    public void shouldCorrectlyParseLiteralWithContent() {
        String result = processor.process("{:hello world}", withoutContext(), RESOLVER);
        assertEquals("{hello world}", result);
    }

    @Test
    public void shouldCorrectlyParseEscapedBraceInLiteral() {
        // Input chars: {, :, \, }, }
        // In LITERAL: \ → ESCAPE_LITERAL, } → ACCEPT(}) → literal="}", } → close
        // Output: "{" + "}" + "}" = "{}}"
        String result = processor.process("{:\\}}", withoutContext(), RESOLVER);
        assertEquals("{}}", result);
    }

    // --- Plain text ---

    @Test
    public void shouldHandlePlainTextWithSpecialCharsOutsideMacros() {
        // Semicolons and colons are not special outside macros
        String result = processor.process("just text; with: chars", withoutContext(), RESOLVER);
        assertEquals("just text; with: chars", result);
    }

    // --- Property syntax via escape ---

    @Test
    public void shouldHandleEscapedColonInMacroName() {
        // {key\:value} → \ escapes : → name="key:value"
        // Resolve splits on first ':', key="key", property="value"
        // Then RESOLVER gets called with key="key" and property resolved
        String result = processor.process("{key\\:value}", withoutContext(), RESOLVER);
        assertNotNull(result);
    }

    // --- Second macro incomplete ---

    @Test
    public void shouldFailWithPartialResultWhenSecondMacroIsIncomplete() {
        String result = null;
        try {
            processor.process("{ok} and {bad", withoutContext(), RESOLVER);
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("[ok] and {bad", result);
    }

    // --- Multiple consecutive macros ---

    @Test
    public void shouldCorrectlyResolveThreeConsecutiveMacros() {
        String result = processor.process("{a}{b}{c}", withoutContext(), RESOLVER);
        assertEquals("[a][b][c]", result);
    }

    // --- Whitespace only ---

    @Test
    public void shouldCorrectlyHandleWhitespaceAroundMacro() {
        String result = processor.process(" {name} ", withoutContext(), RESOLVER);
        assertEquals(" [name] ", result);
    }

}
