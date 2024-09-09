package org.example.codellamacopilot.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.requests.requestparameters.RequestParameters;

public class CustomRequestObject extends RequestObject{
    private String prompt;
    private String suffix;
    private String model;
    private RequestParameters parameters;
    private boolean stream;

    public CustomRequestObject(String model, String prompt, String suffix, RequestParameters requestParameters) {
        this.prompt = prompt;
        this.parameters = requestParameters;
        this.suffix = suffix;
        this.model = model;
        this.stream = false;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("prompt")
    @Override
    public String getInputData() {
        return prompt;
    }

    @JsonProperty("prompt")
    @Override
    public void setInputData(String prompt) {
        this.prompt = prompt;
    }

    @JsonProperty("suffix")
    public String getSuffix() {
        return suffix;
    }

    @JsonProperty("suffix")
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @JsonProperty("options")
    @Override
    public RequestParameters getParameters() {
        return this.parameters;
    }

    @JsonProperty("options")
    @Override
    public void setParameters(RequestParameters parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("stream")
    public Boolean getStream() {
        return stream;
    }

    @JsonProperty("stream")
    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
