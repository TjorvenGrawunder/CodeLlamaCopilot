package org.example.codellamacopilot.chatwindow.requestobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.util.List;

/**
 * Object that is sent to the server
 */
public class ChatRequestObject {
    private String model;
    private List<MessageObject> messages;
    private Boolean stream;

    public ChatRequestObject(String model, List<MessageObject> messages, boolean stream){
        this.model = model;
        this.messages = messages;
        this.stream = stream;
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

    @JsonProperty("stream")
    public Boolean getStream() {
        return stream;
    }

    @JsonProperty("stream")
    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
