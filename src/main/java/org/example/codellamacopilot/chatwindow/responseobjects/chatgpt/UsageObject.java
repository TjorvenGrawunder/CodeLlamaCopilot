package org.example.codellamacopilot.chatwindow.responseobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageObject {
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    @JsonProperty("prompt_tokens")
    public int getPromptTokens() {
        return promptTokens;
    }

    @JsonProperty("prompt_tokens")
    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    @JsonProperty("completion_tokens")
    public int getCompletionTokens() {
        return completionTokens;
    }

    @JsonProperty("completion_tokens")
    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    @JsonProperty("total_tokens")
    public int getTotalTokens() {
        return totalTokens;
    }

    @JsonProperty("total_tokens")
    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }
}
