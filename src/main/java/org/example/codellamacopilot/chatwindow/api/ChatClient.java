package org.example.codellamacopilot.chatwindow.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;
import org.example.codellamacopilot.chatwindow.parser.ResponseParser;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestobjects.chatgpt.ChatGPTRequestObject;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

    public String test(){
        Document currentDocument = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile documentFile = FileDocumentManager.getInstance().getFile(currentDocument);
        String response;
        if(documentFile != null){
            String filePath = documentFile.getPath().replace(".java", "Test.java");
            System.out.println("Document: " + documentFile.getPath());
            response = sendMessage("Please write a JUnit test file for the following code: \n" +currentDocument.getText() + "\n Only provide code.");
            response = response.substring(8, response.length()-3);
            try {
                File file = new File(filePath);
                if(file.createNewFile()){
                    System.out.println("File created: " + file.getAbsolutePath());
                }
                FileUtils.writeStringToFile(file, response, "UTF-8", false);
                response = "File created: " + file.getAbsolutePath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            response = "No file found";
        }

        //String title = response.split("\n")[0];
        //String packageName = title.substring(8, title.length()-1).replaceAll("\\.","/");
        //System.out.println(packageName);


        return response;
    }

}
