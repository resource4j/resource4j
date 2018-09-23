package com.github.resource4j.resources.processors;

import org.junit.Test;

import static com.github.resource4j.resources.context.ResourceResolutionContext.with;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

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

}
