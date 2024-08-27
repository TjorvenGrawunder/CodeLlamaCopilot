package org.example.codellamacopilot.chatwindow.requestformats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.Transient;
import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistoryManipulator;
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatGPTRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ChatGPTResponseObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;

import java.net.URI;
import java.net.http.HttpRequest;

public abstract class AbstractChatRequestFormat implements ChatRequestFormat{

    private final String API_URL;
    private String model;

    //Chat history
    @Transient
    private final ChatHistoryManipulator chatHistory = new ChatHistoryManipulator();

    private final boolean PERSISTENT_CHAT_HISTORY;

    public AbstractChatRequestFormat(String API_URL, String model) {
        this.API_URL = API_URL;
        this.model = model;
        this.PERSISTENT_CHAT_HISTORY = true;
    }

    public AbstractChatRequestFormat(String API_URL, String model, boolean persistentChatHistory) {
        this.API_URL = API_URL;
        this.model = model;
        this.PERSISTENT_CHAT_HISTORY = persistentChatHistory;
    }


    public HttpRequest getRequest(String message) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        MessageObject messageObject = new MessageObject("user", message);
        ChatGPTRequestObject requestObject;
        if(PERSISTENT_CHAT_HISTORY){
            chatHistory.addMessage(messageObject);
            requestObject = new ChatGPTRequestObject(model, chatHistory.getMessages());
        }else{
            requestObject = new ChatGPTRequestObject(model, chatHistory.getSystemPromptsWithMessage(messageObject));
        }

        String apiToken = CopilotSettingsState.getInstance().chatApiToken;

        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken)
                    .header("accept", "application/json")
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public String parseResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        ChatGPTResponseObject responseObject;
        try {
            responseObject = mapper.readValue(response, ChatGPTResponseObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response = responseObject.getChoices()[0].getMessage().getContent();
        if (PERSISTENT_CHAT_HISTORY){
            chatHistory.addMessage(new MessageObject("assistant", response));
        }

        return response;
    }

    public abstract ChatRequestFormat getNewInstance(boolean persistentChatHistory);

    public void addCodeContext(Project project){
        chatHistory.removeCodeContext();
        FileEditor[] editors = FileEditorManager.getInstance(project).getAllEditors();
        for (FileEditor editor : editors) {
            if(editor instanceof TextEditor textEditor){
                chatHistory.addCodeContext(new MessageObject("system", "Background information code:\n" + textEditor.getEditor().getDocument().getText()));
            }
        }
    }

    public String getModel(){
        return this.model;
    }

    public void setModel(String model){
        this.model = model;
    }

    protected String getAPI_URL(){
        return this.API_URL;
    }
}
