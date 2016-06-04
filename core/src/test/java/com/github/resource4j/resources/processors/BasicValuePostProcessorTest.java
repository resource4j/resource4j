package com.github.resource4j.resources.processors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicValuePostProcessorTest {

    public static final ResourceResolver RESOLVER = k -> "[" + k + "]";
    private BasicValuePostProcessor processor = new BasicValuePostProcessor();

    @Test
    public void shouldReturnSameValueWhenNoSubstitutionsPresent() {
        String text = "Simple text";
        String result = processor.process(RESOLVER, text);
        assertEquals(text, result);
    }

    @Test
    public void shouldReturnSubstitutionWhenValueIsAlias() {
        final String alias = "{name}";
        String result = processor.process(RESOLVER, alias);
        assertEquals("[name]", result);
    }

    @Test
    public void shouldReturnEscapedKeyWithoutSubstitution() {
        final String alias = "{:name}";
        String result = processor.process(RESOLVER, alias);
        assertEquals("{name}", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultWhenEscapedKeyIsEmpty() {
        String result = null;
        try {
            processor.process(RESOLVER, "{:}");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("{:}", result);
    }

    @Test
    public void shouldReturnEmptyStringWhenEmptyStringProcessed() {
        String result = processor.process(RESOLVER, "");
        assertEquals("", result);
    }

    @Test
    public void shouldReturnCorrectStringWhenTwoConsequentialValuesProcessed() {
        String result = processor.process(RESOLVER, "{1}{2}");
        assertEquals("[1][2]", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnEscapingAtTheEndOfString() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value \\");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value \\", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnOpeningAtTheEndOfString() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value {");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnIncompleteMacroAtTheEndOfString() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value {test");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {test", result);
    }

    @Test
    public void shouldCorrectlyHandleOpeningAndEscapingAtTheEndOfString() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value {\\");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {\\", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingInMacroName() {
        String result = processor.process(RESOLVER, "Value {1\\0}");
        assertEquals("Value [1\\0]", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingOpening() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value \\{0}");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0}", result);
    }

    @Test
    public void shouldReturnCorrectStringWithEscapingEnding() {
        String result = processor.process(RESOLVER, "Value \\}");
        assertEquals("Value }", result);
    }
    @Test
    public void shouldFailWithCorrectPartialResultOnEscapedEndingInMacro() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value {0\\}");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0}", result);
    }
    @Test
    public void shouldReturnCorrectStringWithEscapingEscapeChar() {
        String result = processor.process(RESOLVER, "Value \\\\");
        assertEquals("Value \\", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnNestedSubstitution() {
        String result = null;
        try {
            processor.process(RESOLVER, "Value {0{1}}");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0[1]}", result);
    }

    @Test
    public void shouldFailWithCorrectPartialResultOnSubstituteNotFound() {
        String result = null;
        try {
            processor.process(k -> "0".equals(k) ? null : "[" + k + "]", "Value {0} {1}");
        } catch (ValuePostProcessingException e) {
            result = e.getPartialResult();
        }
        assertEquals("Value {0} [1]", result);
    }

}
