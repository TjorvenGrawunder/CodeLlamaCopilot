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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class PerplexityAIRequestFormat extends AbstractChatRequestFormat {
    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String MODEL;

    public PerplexityAIRequestFormat() {
        super("https://api.openai.com/v1/chat/completions", "gpt-4o-mini");
        this.MODEL = "gpt-4o-mini";
    }

    public PerplexityAIRequestFormat(String model) {
        super("https://api.perplexity.ai/chat/completions", model);
        this.MODEL = model;
    }

    public PerplexityAIRequestFormat(String model, boolean persistentChatHistory) {
        super("https://api.perplexity.ai/chat/completions", model, persistentChatHistory);
        this.MODEL = model;
    }

    @Override
    public ChatRequestFormat getNewInstance(boolean persistentChatHistory) {
        return new PerplexityAIRequestFormat(this.MODEL, persistentChatHistory);
    }

    @Override
    public String getModel() {
        return this.MODEL;
    }

    @Override
    public String toString() {
        return "PerplexityAI";
    }
}
