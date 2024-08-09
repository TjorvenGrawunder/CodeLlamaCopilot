package org.example.codellamacopilot.chatwindow.requestformats;

import org.example.codellamacopilot.util.CodeSnippet;

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
}
