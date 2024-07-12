package org.example.codellamacopilot.chatwindow.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.example.codellamacopilot.chatwindow.parser.ResponseParser;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatGPTRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatClient {

    private Project project;
    private final ChatRequestFormat REQUEST_FORMAT;
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public ChatClient(Project project, ChatRequestFormat requestFormat) {
        this.project = project;
        this.REQUEST_FORMAT = requestFormat;
    }

    public String sendMessage(String message) {
        HttpRequest request = REQUEST_FORMAT.getRequest(message);
        HttpResponse<String> response;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            return REQUEST_FORMAT.parseResponse(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String explain(){
        Document currentDocument = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        return sendMessage("Please explain the following code: \n" +currentDocument.getText());
    }

    public String debug(){
        Document currentDocument = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        return sendMessage("Please debug the following code: \n" +currentDocument.getText());
    }

}
