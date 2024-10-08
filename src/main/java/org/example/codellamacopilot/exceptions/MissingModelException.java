package org.example.codellamacopilot.exceptions;

/**
 * Exception that is thrown when a model is not configured in the settings.
 */
public class MissingModelException extends RuntimeException {
    public MissingModelException(String message) {
        super(message);
    }
}
