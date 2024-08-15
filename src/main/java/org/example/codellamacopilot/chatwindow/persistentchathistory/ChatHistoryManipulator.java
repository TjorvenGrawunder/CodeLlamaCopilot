package org.example.codellamacopilot.chatwindow.persistentchathistory;

import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;

import java.util.List;

public class ChatHistoryManipulator {
    private ChatHistory.ChatHistoryState state;
    public ChatHistoryManipulator() {
        state = ChatHistory.getInstance().getState();
    }
    public void addMessage(MessageObject message) {
        if (state.messages.size() > 50) {
            state.messages.remove(1);
        }
        state.messages.add(message);
    }

    public List<MessageObject> getMessages() {
        return state.messages;
    }
}
