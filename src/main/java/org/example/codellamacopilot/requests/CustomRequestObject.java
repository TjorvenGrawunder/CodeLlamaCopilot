package org.example.codellamacopilot.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.requests.requestparameters.RequestParameters;

public class CustomRequestObject extends RequestObject{
    private String inputs;
    private RequestParameters parameters;

    public CustomRequestObject(String inputs, RequestParameters requestParameters) {
        this.inputs = inputs;
        this.parameters = requestParameters;
    }

    @JsonProperty("message")
    @Override
    public String getInputData() {
        return inputs;
    }

    @JsonProperty("message")
    @Override
    public void setInputData(String inputData) {
        this.inputs = inputData;
    }

    @JsonProperty("parameters")
    @Override
    public RequestParameters getParameters() {
        return this.parameters;
    }

    @JsonProperty("parameters")
    @Override
    public void setParameters(RequestParameters parameters) {
        this.parameters = parameters;
    }
}
