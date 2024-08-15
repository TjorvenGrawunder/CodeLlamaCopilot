package org.example.codellamacopilot.chatwindow.persistentchathistory;

import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageObjectConverter extends Converter<MessageObject> {
    @Override
    public @Nullable MessageObject fromString(@NotNull String value) {
        String role = value.split(":")[0];
        return new MessageObject(role, value.substring(role.length() + 1));
    }

    @Override
    public @Nullable String toString(@NotNull MessageObject value) {
        return value.toString();
    }
}
