package org.example.codellamacopilot.llamaconnection;

import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface RequestFormat {
    HttpRequest getRequest(CodeSnippet data);
    String parseResponse(String response) throws IOException;
}