package org.example.codellamacopilot.requests.requestparameters;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HuggingFace request parameters for the HuggingFace model.
 */
public class HuggingFaceRequestParameters implements RequestParameters{
    private Integer topK;
    private Float topP;
    private Float temperature;
    private Float repetitionPenalty;
    private Integer maxNewTokens;
    private Float maxTime;
    private Boolean returnFullText;
    private Integer numReturnSequences;
    private Boolean doSample;

    public HuggingFaceRequestParameters(Integer topK, Float topP, Float temperature, Float repetitionPenalty, Integer maxNewTokens, Float maxTime, Boolean returnFullText, Integer numReturnSequences, Boolean doSample) {
        this.topK = topK;
        this.topP = topP;
        this.temperature = temperature;
        this.repetitionPenalty = repetitionPenalty;
        this.maxNewTokens = maxNewTokens;
        this.maxTime = maxTime;
        this.returnFullText = returnFullText;
        this.numReturnSequences = numReturnSequences;
        this.doSample = doSample;
    }


    @JsonProperty("top_k")
    public Integer getTopK() {
        return topK;
    }

    @JsonProperty("top_p")
    public Float getTopP() {
        return topP;
    }

    @JsonProperty("temperature")
    public Float getTemperature() {
        return temperature;
    }

    @JsonProperty("repetition_penalty")
    public Float getRepetitionPenalty() {
        return repetitionPenalty;
    }

    @JsonProperty("max_new_tokens")
    public Integer getMaxNewTokens() {
        return maxNewTokens;
    }

    @JsonProperty("max_time")
    public Float getMaxTime() {
        return maxTime;
    }

    @JsonProperty("return_full_text")
    public Boolean isReturnFullText() {
        return returnFullText;
    }

    @JsonProperty("num_return_sequences")
    public Integer getNumReturnSequences() {
        return numReturnSequences;
    }

    @JsonProperty("do_sample")
    public Boolean isDoSample() {
        return doSample;
    }
}
