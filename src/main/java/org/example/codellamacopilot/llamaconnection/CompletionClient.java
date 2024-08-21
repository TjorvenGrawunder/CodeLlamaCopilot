package org.example.codellamacopilot.llamaconnection;

import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CompletionClient {

    private final CompletionRequestFormat REQUEST_FORMAT;
    private final HttpClient CLIENT;

    public CompletionClient(CompletionRequestFormat completionRequestFormat) {
        this.REQUEST_FORMAT = completionRequestFormat;
        this.CLIENT = HttpClient.newHttpClient();
    }

    public String sendData(CodeSnippet data) throws IOException, InterruptedException {
        HttpRequest request = REQUEST_FORMAT.getRequest(data);
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return REQUEST_FORMAT.parseResponse(response.body());
    }

    public String sendComment(String comment) throws IOException, InterruptedException {
        HttpRequest request = REQUEST_FORMAT.getCommentRequest(comment);
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.body());
        return REQUEST_FORMAT.parseResponse(response.body());
    }
}
