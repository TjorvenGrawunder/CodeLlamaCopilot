package org.example.codellamacopilot.llamaconnection;

import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class that sends data to the completion server and returns the response.
 */
public class CompletionClient {

    private final HttpClient CLIENT;

    /**
     * Constructor for the CompletionClient class.
     * @param completionRequestFormat The completion request format to use.
     */
    public CompletionClient( ) {
        this.CLIENT = HttpClient.newHttpClient();
    }

    /**
     * Sends the data to the completion server and returns the response.
     * @param data The data to send to the completion server.
     * @return The response from the completion server.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public String sendData(CodeSnippet data) throws IOException, InterruptedException {
        //Get current completion request format from the settings
        CompletionRequestFormat requestFormat = CopilotSettingsState.getInstance().getUsedCompletionRequestFormat();
        HttpRequest request = requestFormat.getRequest(data);
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return requestFormat.parseResponse(response.body());
    }
}
