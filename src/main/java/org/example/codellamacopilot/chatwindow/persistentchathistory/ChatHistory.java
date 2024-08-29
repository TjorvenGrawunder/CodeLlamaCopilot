package org.example.codellamacopilot.chatwindow.persistentchathistory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Service
@State(
        name = "org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistory",
        storages = @Storage("ChatHistory.xml")
)
public final class ChatHistory implements PersistentStateComponent<ChatHistory.ChatHistoryState> {


    private ChatHistoryState state = new ChatHistoryState();

    @Override
    public @Nullable ChatHistory.ChatHistoryState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull ChatHistoryState state) {
        this.state = state;
    }

    public static ChatHistory getInstance() {
        return ApplicationManager.getApplication().getService(ChatHistory.class);
    }

    static class ChatHistoryState {
        //System prompts
        public List<MessageObject> systemPrompts;
        //Chat history
        public List<MessageObject> messages;

        public ChatHistoryState() {
            systemPrompts = new ArrayList<>() {
                {
                    add(new MessageObject("system", "You are a java assistant, skilled in explaining complex programming concepts. If you change something in classes, pleas mark changes with comments."));
                }
            };
            messages = new ArrayList<>();
        }
    }
}
