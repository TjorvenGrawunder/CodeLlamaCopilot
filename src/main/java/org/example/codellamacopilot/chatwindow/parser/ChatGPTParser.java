package org.example.codellamacopilot.chatwindow.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ChatGPTResponseObject;


import java.util.List;

public class ChatGPTParser implements ResponseParser{
    @Override
    public String parseResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        ChatGPTResponseObject responseObject = null;
        try {
            responseObject = mapper.readValue(response, ChatGPTResponseObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return responseObject.getChoices()[0].getMessage().getContent();
    }
}
