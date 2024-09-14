package org.example.codellamacopilot.exceptions;

/**
 * Exception that is thrown when an error message is received.
 */
public class ErrorMessageException extends Exception {
    public ErrorMessageException(String message) {
        super(message);
    }
}
