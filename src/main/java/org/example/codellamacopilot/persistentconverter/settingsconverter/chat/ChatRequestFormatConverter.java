package org.example.codellamacopilot.persistentconverter.settingsconverter.chat;

import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.PerplexityAIRequestFormat;
import org.example.codellamacopilot.persistentconverter.settingsconverter.SettingsConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * {@inheritDoc}.
 */
public class ChatRequestFormatConverter extends Converter<ChatRequestFormat> implements SettingsConverter {

    @Override
    public @Nullable ChatRequestFormat fromString(@NotNull String value) {
        String[] split = value.split("@");
        if (split[0].equals("ChatGPT")) {
            return new ChatGPTRequestFormat(split[1]);
        }else if (split[0].equals("PerplexityAI")) {
            return new PerplexityAIRequestFormat(split[1]);
        }else {
            return null;
        }
    }

    @Override
    public @Nullable String toString(@NotNull ChatRequestFormat value) {
        return value + "@" + value.getModel();
    }
}
