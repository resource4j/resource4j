package com.github.resource4j.util.bnf;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CursorProbe {

    private final String text;

    private final Consumer<Integer> callback;

    private int position;

    private String matchingValue = null;

    CursorProbe(String text, int start, Consumer<Integer> callback) {
        this.text = text;
        this.callback = callback;
        this.position = start;
    }

    public CursorProbe expect(String patternString) {
        match(patternString, true);
        return this;
    }

    private Optional<String> match(String patternString, boolean assign) {
        Pattern pattern = Pattern.compile(patternString);
        CharSubSequence view = new CharSubSequence(text, position, text.length());
        Matcher matcher = pattern.matcher(view);
        Optional<String> result = Optional.empty();
        if (matcher.lookingAt()) {
            CharSequence seq = view.subSequence(matcher.start(), matcher.end());
            result = Optional.of(seq.toString());
        }
        if (result.isPresent()) {
            String value = result.get();
            this.position = this.position + value.length();
            if (assign) {
                this.matchingValue = value;
            }
        }
        if (matches()) {
            callback.accept(this.position);
        }
        return result;
    }

    public CursorProbe expectDelimiters() {
        match("\\s+", false);
        return this;
    }

    public CursorProbe maybeDelimiters() {
        match("\\s*", false);
        return this;
    }

    public CursorProbe expectOneOf(String... equivalents) {
        for (String pattern : equivalents) {
            Optional<String> result = match(pattern, true);
            if (result.isPresent()) {
                break;
            }
        }
        return this;
    }

    public boolean matches() {
        return matchingValue != null;
    }

    public Optional<String> match() {
        return Optional.ofNullable(matchingValue);
    }

}

