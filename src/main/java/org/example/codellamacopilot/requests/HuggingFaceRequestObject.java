package org.example.codellamacopilot.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.requests.requestparameters.RequestParameters;

public class HuggingFaceRequestObject extends RequestObject{
    private String inputs;
    private RequestParameters parameters;

    public HuggingFaceRequestObject(String inputs, RequestParameters parameters) {
        this.inputs = inputs;
        this.parameters = parameters;
    }

    @JsonProperty("inputs")
    @Override
    public String getInputData() {
        return inputs;
    }
    @JsonProperty("inputs")
    @Override
    public void setInputData(String inputData) {
        this.inputs = inputData;
    }
    @JsonProperty("parameters")
    @Override
    public RequestParameters getParameters() {
        return parameters;
    }
    @JsonProperty("parameters")
    @Override
    public void setParameters(RequestParameters parameters) {
        this.parameters = parameters;
    }
}
