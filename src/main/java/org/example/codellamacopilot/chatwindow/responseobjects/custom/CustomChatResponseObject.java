package org.example.codellamacopilot.chatwindow.responseobjects.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ErrorObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

/**
 * Chat Response Object that is received from the server of custom models
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomChatResponseObject {
    private MessageObject message;
    private ErrorObject error;

    @JsonProperty("message")
    public MessageObject getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(MessageObject message) {
        this.message = message;
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
