package org.example.codellamacopilot.exceptions;

public class CompletionFailedException extends RuntimeException {
    public CompletionFailedException(String message) {
        super(message);
    }
}
