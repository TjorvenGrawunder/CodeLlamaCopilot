package org.example.codellamacopilot.chatwindow.persistentchathistory;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.annotations.Attribute;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory implements PersistentStateComponent<ChatHistory.ChatHistoryState> {


    static class ChatHistoryState {
        //Chat history
        public List<MessageObject> messages;

        public ChatHistoryState(){
            messages = new ArrayList<>() {
                {
                    add(new MessageObject("system", "You are a java assistant, skilled in explaining complex programming concepts."));
                }
            };
        }
    }

    private ChatHistoryState state = new ChatHistoryState();

    @Override
    public @Nullable ChatHistory.ChatHistoryState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull ChatHistoryState state) {
        this.state = state;
    }

    public void addMessage(MessageObject message) {
        if(state.messages.size() > 50){
            state.messages.remove(1);
        }
        state.messages.add(message);
    }

    public List<MessageObject> getMessages() {
        return state.messages;
    }
}
