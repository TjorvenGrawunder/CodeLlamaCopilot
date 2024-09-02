package org.example.codellamacopilot.chatwindow.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.exceptions.ErrorMessageException;
import org.example.codellamacopilot.settings.CopilotSettingsState;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatClient {

    private final Project PROJECT;
    private ChatRequestFormat requestFormat;
    private final HttpClient CLIENT = HttpClient.newHttpClient();
    private final boolean PERSISTENT_CHAT_HISTORY;

    public ChatClient(Project project, ChatRequestFormat requestFormat, boolean persistentChatHistory) {
        this.PROJECT = project;
        this.requestFormat = requestFormat.getNewInstance(persistentChatHistory);
        this.PERSISTENT_CHAT_HISTORY = persistentChatHistory;
    }

    /**
     * Sends a message to the chat model
     * @param message the message to send
     * @return the response from the chat model
     */
    public String sendMessage(String message) throws IOException, InterruptedException, ErrorMessageException {
        //Get current chat request format from the settings
        requestFormat = CopilotSettingsState.getInstance().getUsedChatRequestFormat(PERSISTENT_CHAT_HISTORY);
        requestFormat.addCodeContext(PROJECT);
        HttpRequest request = requestFormat.getRequest(message);
        HttpResponse<String> response;

        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return requestFormat.parseResponse(response.body());

    }

    public String explain() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
        return sendMessage("Please explain the following code: \n" +currentDocument.getText());
    }

    public String debug() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
        return sendMessage("Please debug the following code: \n" +currentDocument.getText());
    }

    public String test() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
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

    public ChatRequestFormat getRequestFormat(){
        return requestFormat;
    }
}
