package org.example.codellamacopilot.chatwindow.requestformats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.project.Project;
import org.example.codellamacopilot.exceptions.ErrorMessageException;

import java.io.IOException;
import java.net.http.HttpRequest;

/**
 * ChatRequestFormat defines the format of the chat request and response
 */
public interface ChatRequestFormat {
    /**
     * Get the request object from the message
     * @param message the chat message to send to the server
     * @return HttpRequest to send to the server
     */
    HttpRequest getRequest(String message) throws JsonProcessingException;

    /**
     * Get the completion request object from the message (Only used if user wants to use completion with chat model)
     * @param message the chat message to send to the server
     * @return HttpRequest to send to the server
     * @throws JsonProcessingException
     */
    HttpRequest getCompletionRequest(String message) throws JsonProcessingException;

    /**
     * Parse the incoming json response from the server
     * @param response the json response from the server
     * @return the response message as markdown string
     * @throws IOException
     */
    String parseResponse(String response, boolean isCompletion, String currentLine) throws IOException, ErrorMessageException;

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

    String getModel();

    void setModel(String model);

    String getName();

    void removeLastMessage();

}
