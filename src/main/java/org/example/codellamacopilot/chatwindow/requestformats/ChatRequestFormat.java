package org.example.codellamacopilot.chatwindow.requestformats;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import java.io.IOException;
import java.net.http.HttpRequest;

public interface ChatRequestFormat {
    /**
     * Get the request object from the message
     * @param message the chat message to send to the server
     * @return HttpRequest to send to the server
     */
    HttpRequest getRequest(String message);

    /**
     * Parse the incoming json response from the server
     * @param response the json response from the server
     * @return the response message as markdown string
     * @throws IOException
     */
    String parseResponse(String response) throws IOException;

    /**
     * Get a new instance of the ChatRequestFormat
     * @return a new instance of the ChatRequestFormat
     */
    ChatRequestFormat getNewInstance(boolean persistentChatHistory);

    /**
     * Add the code context of the current project to the chat history
     * @param project the current project
     */
    void addCodeContext(Project project);

}
