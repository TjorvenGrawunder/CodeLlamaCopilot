package org.example.codellamacopilot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuggingfaceResponseObject extends ResponseObject{
    private String generatedCode;
    private String error;

    @JsonProperty("generated_text")
    public String getGeneratedCode() {
        return generatedCode;
    }

    @JsonProperty("generated_text")
    public void setGeneratedCode(String generatedCode) {
        this.generatedCode = generatedCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
