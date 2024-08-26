package org.example.codellamacopilot.chatwindow.responseobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChoiceObject {
    private int index;
    private MessageObject message;
    private int logprobs;
    private String finishReason;

    @JsonProperty("index")
    public int getIndex() {
        return index;
    }

    @JsonProperty("index")
    public void setIndex(int index) {
        this.index = index;
    }

    @JsonProperty("message")
    public MessageObject getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(MessageObject message) {
        this.message = message;
    }

    @JsonProperty("logprobs")
    public int getLogprobs() {
        return logprobs;
    }

    @JsonProperty("logprobs")
    public void setLogprobs(int logprobs) {
        this.logprobs = logprobs;
    }

    @JsonProperty("finish_reason")
    public String getFinishReason() {
        return finishReason;
    }

    @JsonProperty("finish_reason")
    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
