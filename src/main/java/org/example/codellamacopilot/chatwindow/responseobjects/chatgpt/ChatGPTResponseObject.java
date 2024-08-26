package org.example.codellamacopilot.chatwindow.responseobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGPTResponseObject {
    private String id;
    private String object;
    private int created;
    private String model;
    private ChoiceObject[] choices;
    private UsageObject usage;
    private String systemFingerprint;
    private ErrorObject error;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("object")
    public String getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(String object) {
        this.object = object;
    }

    @JsonProperty("created")
    public int getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(int created) {
        this.created = created;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("choices")
    public ChoiceObject[] getChoices() {
        return choices;
    }

    @JsonProperty("choices")
    public void setChoices(ChoiceObject[] choices) {
        this.choices = choices;
    }

    @JsonProperty("usage")
    public UsageObject getUsage() {
        return usage;
    }

    @JsonProperty("usage")
    public void setUsage(UsageObject usage) {
        this.usage = usage;
    }

    @JsonProperty("system_fingerprint")
    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    @JsonProperty("system_fingerprint")
    public void setSystemFingerprint(String systemFingerprint) {
        this.systemFingerprint = systemFingerprint;
    }

    @JsonProperty("error")
    public ErrorObject getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(ErrorObject error) {
        this.error = error;
    }
}
