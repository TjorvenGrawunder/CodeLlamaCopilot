package org.example.codellamacopilot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuggingfaceResponseObject extends ResponseObject {
    private java.lang.String generatedCode;
    private java.lang.String error;

    @JsonProperty("generated_text")
    public java.lang.String getGeneratedCode() {
        return generatedCode;
    }

    @JsonProperty("generated_text")
    public void setGeneratedCode(java.lang.String generatedCode) {
        this.generatedCode = generatedCode;
    }

    @JsonProperty("error")
    public java.lang.String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(java.lang.String error) {
        this.error = error;
    }
}
