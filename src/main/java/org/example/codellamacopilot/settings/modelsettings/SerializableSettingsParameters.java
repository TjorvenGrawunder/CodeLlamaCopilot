package org.example.codellamacopilot.settings.modelsettings;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SerializableSettingsParameters {
    private String apiToken;
    private String selectedModel;
    private String url;

    public SerializableSettingsParameters(@JsonProperty("url") String url, @JsonProperty("apiToken") String apiToken, @JsonProperty("model") String selectedModel) {
        this.apiToken = apiToken;
        this.selectedModel = selectedModel;
        this.url = url;
    }

    @JsonProperty("apiToken")
    public String getApiToken() {
        return apiToken;
    }
    @JsonProperty("apiToken")
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
    @JsonProperty("model")
    public String getSelectedModel() {
        return selectedModel;
    }
    @JsonProperty("model")
    public void setSelectedModel(String selectedModel) {
        this.selectedModel = selectedModel;
    }
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }
}
