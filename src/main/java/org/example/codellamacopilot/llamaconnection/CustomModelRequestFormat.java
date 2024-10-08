package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codellamacopilot.exceptions.CompletionFailedException;
import org.example.codellamacopilot.requests.CustomRequestObject;
import org.example.codellamacopilot.requests.HuggingFaceRequestObject;
import org.example.codellamacopilot.requests.RequestObject;
import org.example.codellamacopilot.requests.requestparameters.CustomRequestParameters;
import org.example.codellamacopilot.responses.CustomCompletionResponseObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;

import java.net.URI;
import java.net.http.HttpRequest;

public class CustomModelRequestFormat implements CompletionRequestFormat {
    private String model;
    private StringBuilder apiURL = new StringBuilder();
    private String prefixIdentifier = "<Pref>";
    private String suffixIdentifier = "<SUF>";
    private String midIdentifier = "<MID>";

    public CustomModelRequestFormat(String apiURL, String model) {
        this.apiURL.append(apiURL);
        this.model = model;
    }

    @Override
    public HttpRequest getRequest(CodeSnippet code) throws JsonProcessingException {
        CustomRequestObject requestObject = new CustomRequestObject(model, code.prefix(), code.suffix(), new CustomRequestParameters(0));
        return getHttpRequest(requestObject, apiURL);
    }

    @Override
    public String parseResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CustomCompletionResponseObject responseObject;

        responseObject = mapper.readValue(response, CustomCompletionResponseObject.class);

        if (responseObject.getGeneratedCode() == null || response == null) {
            if (responseObject.getError() != null) {
                throw new CompletionFailedException(responseObject.getError());
            } else {
                throw new CompletionFailedException("Response or generated code is null");
            }
        }

        return responseObject.getGeneratedCode();

    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getName() {
        return "Custom";
    }

}
