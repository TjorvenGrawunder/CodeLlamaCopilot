package org.example.codellamacopilot.chatwindow.requestformats;

public class PerplexityAIRequestFormat extends AbstractChatRequestFormat {
    private final String MODEL;

    public PerplexityAIRequestFormat(String model) {
        super("https://api.perplexity.ai/chat/completions", model);
        this.MODEL = model;
    }

    public PerplexityAIRequestFormat(String model, boolean persistentChatHistory) {
        super("https://api.perplexity.ai/chat/completions", model, persistentChatHistory);
        this.MODEL = model;
    }

    @Override
    public ChatRequestFormat getNewInstance(boolean persistentChatHistory) {
        return new PerplexityAIRequestFormat(this.MODEL, persistentChatHistory);
    }

    @Override
    public String getModel() {
        return this.MODEL;
    }

    @Override
    public String getName() {
        return "PerplexityAIRequestFormat";
    }

    @Override
    public String toString() {
        return "PerplexityAI";
    }
}
