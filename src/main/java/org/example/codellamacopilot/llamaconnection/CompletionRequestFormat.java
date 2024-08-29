package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codellamacopilot.requests.RequestObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public interface CompletionRequestFormat {
    HttpRequest getRequest(CodeSnippet data) throws JsonProcessingException;
    String parseResponse(String response) throws IOException;
    void setModel(String model);
    String getName();


    /**
     * Get the HttpRequest object for the request
     * @param requestObject The request object
     * @param apiURL The URL to send the request to
     * @return The HttpRequest object
     */
    default HttpRequest getHttpRequest(RequestObject requestObject, StringBuilder apiURL) throws JsonProcessingException {
        // Get API Token for the currently used model
        String apiToken = CopilotSettingsState.getInstance().apiToken;
        ObjectMapper mapper = new ObjectMapper();
        // Don't include null values in the JSON
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return HttpRequest.newBuilder()
                .uri(URI.create(apiURL.toString()))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken).build();

    }
}
