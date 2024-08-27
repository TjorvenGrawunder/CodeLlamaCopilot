package org.example.codellamacopilot.requests.requestparameters;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomRequestParameters implements RequestParameters {
    private String model;

    public CustomRequestParameters(String model) {
        if(model.isEmpty()){
            this.model = null;
        }else{
            this.model = model;
        }
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }
}
