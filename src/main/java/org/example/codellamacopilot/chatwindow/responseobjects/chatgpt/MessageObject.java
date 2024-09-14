package org.example.codellamacopilot.chatwindow.responseobjects.chatgpt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Message that consists of a role and content.
 * This MessageObject will be sent to the server and saved in the chat history
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageObject implements Serializable {
    private String role;
    private String content;

    public MessageObject(){}

    public MessageObject(String role, String content){
        this.content = content;
        this.role = role;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return role + ":" + content;
    }
}
