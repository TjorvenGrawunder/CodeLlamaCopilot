package org.example.codellamacopilot.responses;

/**
 * Response object for the model.
 */
public abstract class ResponseObject {

    public abstract java.lang.String getGeneratedCode();
    public abstract void setGeneratedCode(java.lang.String generatedCode);
    public abstract java.lang.String getError();
    public abstract void setError(java.lang.String error);
}
