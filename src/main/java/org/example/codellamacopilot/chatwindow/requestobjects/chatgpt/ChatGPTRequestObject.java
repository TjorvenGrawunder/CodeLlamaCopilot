package org.example.codellamacopilot.chatwindow.requestobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.util.List;

public class ChatGPTRequestObject {
    private String model;
    private List<MessageObject> messages;

    public ChatGPTRequestObject(String model, List<MessageObject> messages){
        this.model = model;
        this.messages = messages;
    }
    @JsonProperty("messages")
    public List<MessageObject> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<MessageObject> messages) {
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
