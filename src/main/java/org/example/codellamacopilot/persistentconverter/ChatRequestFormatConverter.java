package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatRequestFormatConverter extends Converter<ChatRequestFormat> {

    @Override
    public @Nullable ChatRequestFormat fromString(@NotNull String value) {
        if (value.equals("ChatGPT")) {
            return new ChatGPTRequestFormat();
        }else{
            return null;
        }
    }

    @Override
    public @Nullable String toString(@NotNull ChatRequestFormat value) {
        return value.toString();
    }
}
