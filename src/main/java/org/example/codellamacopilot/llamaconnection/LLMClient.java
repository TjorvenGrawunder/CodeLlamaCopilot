package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.jr.ob.JSON;
import com.intellij.openapi.progress.ProgressManager;
import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class LLMClient {

    private final RequestFormat REQUEST_FORMAT;
    private final HttpClient CLIENT;

    public LLMClient(RequestFormat requestFormat) {
        this.REQUEST_FORMAT = requestFormat;
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
