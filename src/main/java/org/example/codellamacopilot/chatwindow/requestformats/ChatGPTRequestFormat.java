package org.example.codellamacopilot.chatwindow.requestformats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistoryManipulator;
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatGPTRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ChatGPTResponseObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;

import java.net.URI;
import java.net.http.HttpRequest;

public class ChatGPTRequestFormat extends AbstractChatRequestFormat {

    private final String MODEL;

    public ChatGPTRequestFormat(String model) {
        super("https://api.openai.com/v1/chat/completions", model);
        this.MODEL = model;
    }

    public ChatGPTRequestFormat(String model, boolean persistentChatHistory) {
        super("https://api.openai.com/v1/chat/completions", model, persistentChatHistory);
        this.MODEL = model;
    }


    @Override
    public ChatRequestFormat getNewInstance(boolean persistentChatHistory) {
        return new ChatGPTRequestFormat(this.MODEL, persistentChatHistory);
    }

    @Override
    public String getModel(){
        return this.MODEL;
    }

    @Override
    public String getName() {
        return "ChatGPTRequestFormat";
    }

    @Override
    public String toString() {
        return "ChatGPT";
    }

}
