package org.example.codellamacopilot.chatwindow.requestformats;

public class ChatGPTRequestFormat extends AbstractChatRequestFormat {

    private final String MODEL;

    public ChatGPTRequestFormat(String model) {
        super("https://api.openai.com/v1/chat/completions", model);
        this.MODEL = model;
    }

    public ChatGPTRequestFormat(String model, boolean persistentChatHistory) {
        super("https://api.openai.com/v1/chat/completions", model, persistentChatHistory);
        this.MODEL = model;
    }


    @Override
    public ChatRequestFormat getNewInstance(boolean persistentChatHistory) {
        return new ChatGPTRequestFormat(this.MODEL, persistentChatHistory);
    }

    @Override
    public String getModel(){
        return this.MODEL;
    }

    @Override
    public String getName() {
        return "ChatGPTRequestFormat";
    }

    @Override
    public String toString() {
        return "ChatGPT";
    }

}
