package org.example.codellamacopilot.chatwindow.requestobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

public class ChatGPTRequestObject {
    private String model;
    private MessageObject[] messages;

    public ChatGPTRequestObject(String model, MessageObject[] messages){
        this.model = model;
        this.messages = messages;
    }
    @JsonProperty("messages")
    public MessageObject[] getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(MessageObject[] messages) {
        this.messages = messages;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }
}
