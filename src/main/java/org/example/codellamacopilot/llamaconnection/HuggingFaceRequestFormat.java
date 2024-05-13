package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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

public class HuggingFaceRequestFormat implements RequestFormat, Serializable {

    private final String API_URL = "https://api-inference.huggingface.co/models/codellama/CodeLlama-7b-hf";
    private final String LLAMA3_URL = "https://api-inference.huggingface.co/models/meta-llama/Meta-Llama-3-70B";

    @Override
    public HttpRequest getRequest(CodeSnippet code) {
        String data = String.format("<Pref> %s <SUF> %s <MID>", code.prefix(), code.suffix());
        HuggingFaceRequestObject requestObject = new HuggingFaceRequestObject(data, new HuggingFaceRequestParameters(
                null, null, null, 0.9f, 250, 0.01f,
                false, null, null));
        String apiToken = CopilotSettingsState.getInstance().apiToken;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestObject)))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String parseResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        List<HuggingfaceResponseObject> list = mapper.readValue(response, typeFactory.constructCollectionType(List.class, HuggingfaceResponseObject.class));
        return list.get(0).getGeneratedCode().replaceAll("<EOT>","");
    }

    @Override
    public String toString() {
        return "CodeLlama-7b-hf";
    }
}
