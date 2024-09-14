package org.example.codellamacopilot.exceptions;

/**
 * Exception that is thrown when a completion fails.
 */
public class CompletionFailedException extends RuntimeException {
    public CompletionFailedException(String message) {
        super(message);
    }
}
