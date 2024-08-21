package org.example.codellamacopilot.chatwindow.persistentchathistory;

import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryManipulator {

    private ChatHistory.ChatHistoryState state;
    private int contextCounter = 1;

    public ChatHistoryManipulator() {
        state = ChatHistory.getInstance().getState();
    }
    public void addMessage(MessageObject message) {
        if (state.messages.size() > 50) {
            state.messages.remove(contextCounter);
        }
        state.messages.add(message);
    }

    public void removeLastMessage() {
        state.messages.remove(state.messages.size() - 1);
    }

    public void removeCodeContext() {
        int index = 1;
        while (state.systemPrompts.size() > 1) {
            state.systemPrompts.remove(index);
        }
        resetContextCounter();
    }

    public void addCodeContext(MessageObject context) {
        state.systemPrompts.add(contextCounter, context);
        incrementContextCounter();
    }

    public void incrementContextCounter() {
        contextCounter++;
    }

    public void decrementContextCounter() {
        contextCounter--;
    }

    public void resetContextCounter() {
        contextCounter = 1;
    }

    public int getContextCounter() {
        return contextCounter;
    }

    /**
     * Get the system prompts with the message. Used for code generation from comments
     * @param message that should not be included in the history
     * @return the system prompts with the message
     */
    public List<MessageObject> getSystemPromptsWithMessage(MessageObject message) {
        List<MessageObject> messages = new ArrayList<>(state.systemPrompts);
        messages.add(message);
        return messages;
    }

    /**
     * Get the full chat history with prompts and messages
     * @return the full chat history
     */
    public List<MessageObject> getMessages() {
        List<MessageObject> fullHistory = new ArrayList<>();
        fullHistory.addAll(state.systemPrompts);
        fullHistory.addAll(state.messages);
        return fullHistory;
    }
}
