package org.example.codellamacopilot.responses;

public abstract class ResponseObject {

    public abstract String getGeneratedCode();
    public abstract void setGeneratedCode(String generatedCode);
    public abstract String getError();
    public abstract void setError(String error);
}
