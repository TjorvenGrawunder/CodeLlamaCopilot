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
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.ChatGPTResponseObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.chatwindow.responseobjects.custom.CustomChatResponseObject;
import org.example.codellamacopilot.exceptions.ErrorMessageException;
import org.example.codellamacopilot.settings.CopilotSettingsState;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

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


    public HttpRequest getRequest(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        MessageObject messageObject = new MessageObject("user", message);
        ChatRequestObject requestObject;
        if(PERSISTENT_CHAT_HISTORY){
            chatHistory.addMessage(messageObject);
            requestObject = new ChatRequestObject(model, chatHistory.getMessages(), false);
        }else{
            requestObject = new ChatRequestObject(model, chatHistory.getSystemPromptsWithMessage(messageObject), false);
        }

        String apiToken = CopilotSettingsState.getInstance().chatApiToken;

        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken)
                .header("accept", "application/json")
                .build();
    }

    public HttpRequest getCompletionRequest(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        List<MessageObject> prompts = new ArrayList<>(){
            {
                add(new MessageObject("system", "You are an completion assistant that fill in the gap " +
                        "between provided prefix and suffix. If the prefix ends with a comment you should implement the content of" +
                        "this comment. " +
                        "Please only provide your generated code in the middle and dont use markdown."));
            }
        };

        prompts.addAll(chatHistory.getCodeContext());
        prompts.add(new MessageObject("user", message));

        ChatRequestObject requestObject;
        requestObject = new ChatRequestObject(model, prompts, false);

        String apiToken = CopilotSettingsState.getInstance().chatApiToken;

        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken)
                .header("accept", "application/json")
                .build();
    }

    public String parseResponse(String response) throws JsonProcessingException, ErrorMessageException {
        ObjectMapper mapper = new ObjectMapper();
        if(CopilotSettingsState.getInstance().usedChatModel.equals("Custom")){
            CustomChatResponseObject responseObject;
            responseObject = mapper.readValue(response, CustomChatResponseObject.class);

            if(responseObject.getMessage() == null) {
                throw new ErrorMessageException(responseObject.getError().getMessage());
            }else {
                response = responseObject.getMessage().getContent();
            }
        }else {
            ChatGPTResponseObject responseObject;
            responseObject = mapper.readValue(response, ChatGPTResponseObject.class);

            if (responseObject.getChoices() == null) {
                throw new ErrorMessageException(responseObject.getError().getMessage());
            } else {
                response = responseObject.getChoices()[0].getMessage().getContent();
            }
        }



        if (PERSISTENT_CHAT_HISTORY && response != null) {
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

    public void removeLastMessage(){
        chatHistory.removeLastMessage();
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
