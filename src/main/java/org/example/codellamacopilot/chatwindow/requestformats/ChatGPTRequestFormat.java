package org.example.codellamacopilot.chatwindow.requestformats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatGPTRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ChatGPTResponseObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTRequestFormat implements ChatRequestFormat {

    private final String API_URL = "https://api.openai.com/v1/chat/completions";

    List<MessageObject> messages = new ArrayList<>() {
        {
            add(new MessageObject("system", "You are a java assistant, skilled in explaining complex programming concepts."));
            //add(new MessageObject("system", "You are a java programmer, skilled in programming. You are not allowed to write other text than Code."));
        }
    };

    @Override
    public HttpRequest getRequest(String message) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        messages.add(new MessageObject("user", message));
        ChatGPTRequestObject requestObject = new ChatGPTRequestObject("gpt-4o-mini", messages);

        String apiToken = CopilotSettingsState.getInstance().chatApiToken;

        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String parseResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        ChatGPTResponseObject responseObject;
        try {
            responseObject = mapper.readValue(response, ChatGPTResponseObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response = responseObject.getChoices()[0].getMessage().getContent();
        messages.add(new MessageObject("assistant", response));
        return response;
    }

    @Override
    public String toString() {
        return "ChatGPT";
    }
}
