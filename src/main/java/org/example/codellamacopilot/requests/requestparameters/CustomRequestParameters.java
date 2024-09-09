package org.example.codellamacopilot.requests.requestparameters;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomRequestParameters implements RequestParameters {
    private int temperature;

    public CustomRequestParameters(int temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("temperature")
    public int getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
