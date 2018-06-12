package com.github.resource4j.resources.processors;

import com.github.resource4j.ResourceException;

public class ValuePostProcessingException extends ResourceException {

    private static final long serialVersionUID = 1;

    private final String partialResult;

    public ValuePostProcessingException(String partialResult) {
        super("Post-processing failed with result:" + partialResult);
        this.partialResult = partialResult;
    }

    public String getPartialResult() {
        return partialResult;
    }

}
