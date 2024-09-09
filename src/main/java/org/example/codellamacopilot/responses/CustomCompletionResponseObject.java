package org.example.codellamacopilot.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomCompletionResponseObject extends ResponseObject {
    private java.lang.String generatedCode;
    private java.lang.String error;

    @JsonProperty("response")
    public java.lang.String getGeneratedCode() {
        return generatedCode;
    }

    @JsonProperty("response")
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
