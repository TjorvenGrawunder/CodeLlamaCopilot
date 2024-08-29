package org.example.codellamacopilot.llamaconnection;

import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CompletionClient {

    private CompletionRequestFormat requestFormat;
    private final HttpClient CLIENT;

    public CompletionClient(CompletionRequestFormat completionRequestFormat) {
        this.requestFormat = completionRequestFormat;
        this.CLIENT = HttpClient.newHttpClient();
    }

    public String sendData(CodeSnippet data) throws IOException, InterruptedException {
        //Get current completion request format from the settings
        requestFormat = CopilotSettingsState.getInstance().getUsedCompletionRequestFormat();
        HttpRequest request = requestFormat.getRequest(data);
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return requestFormat.parseResponse(response.body());
    }
}
