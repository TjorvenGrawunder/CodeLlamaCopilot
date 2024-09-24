package org.example.codellamacopilot.chatwindow.persistentchathistory;

import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manipulate the chat history of the assistant
 */
public class ChatHistoryManipulator {

    private ChatHistory.ChatHistoryState state;
    private int contextCounter = 1;

    public ChatHistoryManipulator() {
        state = ChatHistory.getInstance().getState();
    }

    /**
     * Add a message to the chat history.
     * If the chat history is full, remove the two oldest messages
     * @param message the message to add
     */
    public void addMessage(MessageObject message) {
        if (state.messages.size() > 50) {
            state.messages.remove(0);
            state.messages.remove(0);
        }
        state.messages.add(message);
    }

    /**
     * Remove the last message from the chat history.
     * Used if an error occurred and the last message should not be included in the history
     */
    public void removeLastMessage() {
        if(state.messages.size() % 2 == 1) {
            state.messages.remove(state.messages.size() - 1);
        }
    }

    /**
     * Remove all code context from the chat history
     */
    public void removeCodeContext() {
        int index = 1;
        while (state.systemPrompts.size() > 1) {
            state.systemPrompts.remove(index);
        }
        resetContextCounter();
    }

    /**
     * Add new code context of the current project to the chat history
     * @param context the code context to add
     */
    public void addCodeContext(MessageObject context) {
        state.systemPrompts.add(contextCounter, context);
        incrementContextCounter();
    }

    /**
     * Get the code context list of the current project
     * @return the code context list
     */
    public List<MessageObject> getCodeContext() {
        return state.systemPrompts.subList(1, state.systemPrompts.size());
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

    /**
     * Clear the chat history
     */
    public void clearChatHistory() {
        state.messages.clear();
    }

    /**
     * Only change first system prompt
     * @param prompt the new prompt
     */
    public void setSystemPrompt(String prompt){
        state.systemPrompts.get(0).setContent(prompt);
    }

    /**
     * Get the first system prompt
     * @return the content of the first system prompt
     */
    public String getFirstSystemPrompt(){
        return state.systemPrompts.get(0).getContent();
    }
}
