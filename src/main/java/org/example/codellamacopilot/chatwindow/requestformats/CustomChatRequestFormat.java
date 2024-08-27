package org.example.codellamacopilot.chatwindow.requestformats;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.net.http.HttpRequest;

public class CustomChatRequestFormat extends AbstractChatRequestFormat{

    public CustomChatRequestFormat(String API_URL, String model) {
        super(API_URL, model);
    }

    public CustomChatRequestFormat(String API_URL, String model, boolean persistentChatHistory) {
        super(API_URL, model, persistentChatHistory);
    }

    @Override
    public ChatRequestFormat getNewInstance(boolean persistentChatHistory) {
        return new CustomChatRequestFormat(getAPI_URL(), getModel(), persistentChatHistory);
    }

    @Override
    public String getName() {
        return "Custom";
    }
}
