package org.example.codellamacopilot.chatwindow.requestformats;

import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface ChatRequestFormat {
    HttpRequest getRequest(String message);
    String parseResponse(String response) throws IOException;
}
