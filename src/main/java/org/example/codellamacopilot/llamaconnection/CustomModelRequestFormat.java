package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codellamacopilot.requests.CustomRequestObject;
import org.example.codellamacopilot.requests.HuggingFaceRequestObject;
import org.example.codellamacopilot.requests.RequestObject;
import org.example.codellamacopilot.requests.requestparameters.CustomRequestParameters;
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

    public CustomModelRequestFormat(String apiURL) {
        this.apiURL.append(apiURL);
    }

    @Override
    public HttpRequest getRequest(CodeSnippet code) {
        String data = String.format("%s %s %s %s %s", prefixIdentifier, code.prefix(), suffixIdentifier, code.suffix(), midIdentifier);
        CustomRequestObject requestObject = new CustomRequestObject(data, new CustomRequestParameters(model));
        return getHttpRequest(requestObject, apiURL);
    }

    @Override
    public String parseResponse(String response) {
        return null;
    }

    @Override
    public HttpRequest getCommentRequest(String comment) {
        throw new UnsupportedOperationException("Not supported in CompletionRequestFormat classes");
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
