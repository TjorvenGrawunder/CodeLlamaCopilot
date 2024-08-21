package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.codellamacopilot.completionutil.CompletionPropositionsStorage;
import org.example.codellamacopilot.requests.HuggingFaceRequestObject;
import org.example.codellamacopilot.requests.requestparameters.HuggingFaceRequestParameters;
import org.example.codellamacopilot.responses.HuggingfaceResponseObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class HuggingFaceRequestFormat implements CompletionRequestFormat, Serializable {

    private final StringBuilder API_URL = new StringBuilder("https://api-inference.huggingface.co/models/");
    private boolean appended = false;

    public HuggingFaceRequestFormat() {
        API_URL.append("codellama/CodeLlama-7b-hf");
        appended = true;
    }

    public HuggingFaceRequestFormat(String model) {
        API_URL.append(model);
        appended = true;
    }


    @Override
    public HttpRequest getRequest(CodeSnippet code) {
        String data = String.format("<Pref> %s <SUF> %s <MID>", code.prefix(), code.suffix());
        HuggingFaceRequestObject requestObject = new HuggingFaceRequestObject(data, new HuggingFaceRequestParameters(
                null, null, null, 0.9f, 250, 0.01f,
                false, 3, null));
        return getHttpRequest(requestObject);
    }

    @Override
    public String parseResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        List<HuggingfaceResponseObject> list = mapper.readValue(response, typeFactory.constructCollectionType(List.class, HuggingfaceResponseObject.class));
        CompletionPropositionsStorage.clearPropositions();
        for (int i = 1; i < list.size(); i++) {
            CompletionPropositionsStorage.addProposition(list.get(i).getGeneratedCode().replaceAll("<EOT>",""));
        }
        return list.get(0).getGeneratedCode().replaceAll("<EOT>","");
    }

    @Override
    public HttpRequest getCommentRequest(String comment) {
        comment = "Please implement the following comment in java! Comment: " + comment +
                "The implemtation of the comment is ";
        HuggingFaceRequestObject requestObject = new HuggingFaceRequestObject(comment, new HuggingFaceRequestParameters(
                null, null, null, 0.9f, 250, 0.01f,
                false, null, null));
        return getHttpRequest(requestObject);
    }

    private HttpRequest getHttpRequest(HuggingFaceRequestObject requestObject) {
        String apiToken = CopilotSettingsState.getInstance().apiToken;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(API_URL.toString()))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setModel(String model) {
        if (appended) {
            API_URL.delete(0, API_URL.length());
            appended = false;
        }
        API_URL.append("https://api-inference.huggingface.co/models/").append(model);
        appended = true;
    }

    @Override
    public String toString() {
        return "HuggingFace";
    }
}