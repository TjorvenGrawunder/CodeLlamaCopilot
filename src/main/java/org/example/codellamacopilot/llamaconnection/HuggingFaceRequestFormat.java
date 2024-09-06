package org.example.codellamacopilot.llamaconnection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.codellamacopilot.completionutil.CompletionPropositionsStorage;
import org.example.codellamacopilot.exceptions.CompletionFailedException;
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
import java.util.Objects;

public class HuggingFaceRequestFormat implements CompletionRequestFormat, Serializable {

    private final StringBuilder API_URL = new StringBuilder("https://api-inference.huggingface.co/models/");
    private boolean appended = false;
    String prefix = "<Pref>";
    String suffix = "<SUF>";
    String middle = "<MID>";

    public HuggingFaceRequestFormat() {
        this("codellama/CodeLlama-7b-hf");
    }

    public HuggingFaceRequestFormat(String model) {
        API_URL.append(model);
        appended = true;
        choosePrefixSuffixIdentifier(model);
    }


    @Override
    public HttpRequest getRequest(CodeSnippet code) throws JsonProcessingException {
        String data = String.format("%s %s %s %s %s", prefix, code.prefix(), suffix, code.suffix(), middle);
        HuggingFaceRequestObject requestObject = new HuggingFaceRequestObject(data, new HuggingFaceRequestParameters(
                null, null, null, 0.9f, 250, 0.01f,
                false, null, null));
        return getHttpRequest(requestObject, API_URL);
    }

    @Override
    public String parseResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        System.out.println(response);
        //TODO: Check for List before parsing
        List<HuggingfaceResponseObject> list = mapper.readValue(response, typeFactory.constructCollectionType(List.class, HuggingfaceResponseObject.class));
        CompletionPropositionsStorage.clearPropositions();
        for (int i = 1; i < list.size(); i++) {
            CompletionPropositionsStorage.addProposition(list.get(i).getGeneratedCode().replaceAll("<EOT>",""));
        }
        if (!Objects.equals(list.get(0).getGeneratedCode(), "")) {
            return list.get(0).getGeneratedCode().replaceAll("<EOT>","");
        } else {
            throw new CompletionFailedException(list.get(0).getError());
        }
    }

    /*@Override
    public HttpRequest getCommentRequest(String comment) {
        comment = "Please implement the following comment in java! Comment: " + comment +
                "The implemtation of the comment is ";
        HuggingFaceRequestObject requestObject = new HuggingFaceRequestObject(comment, new HuggingFaceRequestParameters(
                null, null, null, 0.9f, 250, 0.01f,
                false, null, null));
        return getHttpRequest(requestObject);
    }*/

    public void setModel(String model) {
        if (appended) {
            API_URL.delete(0, API_URL.length());
            appended = false;
        }
        API_URL.append("https://api-inference.huggingface.co/models/").append(model);
        appended = true;
    }

    private void choosePrefixSuffixIdentifier(String model){
        model = model.split("/")[0];
        if(model.equals("codellama") || model.equals("meta-llama")){
            this.prefix = "<Pref>";
            this.suffix = "<SUF>";
            this.middle = "<MID>";
        }else if (model.equals("bigcode")) {
            this.prefix = "<fim_prefix>";
            this.suffix = "<fim_suffix>";
            this.middle = "<fim_middle>";
        }
    }

    @Override
    public String getName() {
        return "HuggingFaceRequestFormat";
    }

    @Override
    public String toString() {
        return "HuggingFace";
    }
}