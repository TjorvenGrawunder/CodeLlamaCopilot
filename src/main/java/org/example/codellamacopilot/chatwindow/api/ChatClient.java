package org.example.codellamacopilot.chatwindow.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
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
     * Sends a message to the chat model, without completion request
     * @param message the message to send
     * @return the response from the chat model
     */
    public String sendMessage(String message) throws IOException, InterruptedException, ErrorMessageException {
        return sendMessage(message, false, "");
    }

    /**
     * Sends a message to the chat model
     * @param message the message to send
     * @param completionRequest if true, the message is a completion request
     * @return the response from the chat model
     */
    public String sendMessage(String message, boolean completionRequest, String currentLine) throws IOException, InterruptedException, ErrorMessageException {
        //Get current chat request format from the settings
        requestFormat = CopilotSettingsState.getInstance().getUsedChatRequestFormat(PERSISTENT_CHAT_HISTORY);
        requestFormat.addCodeContext(PROJECT);
        HttpRequest request;
        if (completionRequest) {
            request = requestFormat.getCompletionRequest(message);
        }else{
            request = requestFormat.getRequest(message);
        }

        HttpResponse<String> response;

        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return requestFormat.parseResponse(response.body(), completionRequest, currentLine);
    }

    /**
     * Explains the code in the current opened editor
     * @return the response from the chat model
     * @throws IOException if the file cannot be read
     * @throws InterruptedException if the request is interrupted
     * @throws ErrorMessageException if the response is an error message
     */
    public String explain() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
        return sendMessage("Please explain the following code: \n" +currentDocument.getText());
    }

    /**
     * Debugs the code in the current opened editor
     * @return the response from the chat model
     * @throws IOException if the file cannot be read
     * @throws InterruptedException if the request is interrupted
     * @throws ErrorMessageException if the response is an error message
     */
    public String debug() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
        return sendMessage("Please debug the following code: \n" +currentDocument.getText());
    }

    /**
     * Creates a junit test file for the code in the current opened editor
     * @return the response from the chat model
     * @throws IOException if the file cannot be read
     * @throws InterruptedException if the request is interrupted
     * @throws ErrorMessageException if the response is an error message
     */
    public String test() throws IOException, InterruptedException, ErrorMessageException {
        Document currentDocument = FileEditorManager.getInstance(PROJECT).getSelectedTextEditor().getDocument();
        VirtualFile documentFile = FileDocumentManager.getInstance().getFile(currentDocument);
        String response;
        if(documentFile != null){
            String filePath = documentFile.getPath().replace(".java", "Test.java");
            response = sendMessage("Please write a JUnit test file for the following code: \n" +currentDocument.getText() + "\n Only provide code.");
            response = response.substring(8, response.length()-3);
            try {
                File file = new File(filePath);
                file.createNewFile();
                FileUtils.writeStringToFile(file, response, "UTF-8", false);
                response = "File created: " + file.getAbsolutePath();

                // Refresh the file system to reflect the new file
                VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
                if (virtualFile != null) {
                    virtualFile.refresh(true, false);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            response = "No file found";
        }

        return response;
    }

    public ChatRequestFormat getRequestFormat(){
        return requestFormat;
    }
}
