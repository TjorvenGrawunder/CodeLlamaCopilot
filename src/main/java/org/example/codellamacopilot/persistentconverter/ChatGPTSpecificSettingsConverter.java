package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xmlb.Converter;
import com.teamdev.jxbrowser.deps.org.checkerframework.checker.units.qual.C;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatGPTSpecificSettingsConverter extends Converter<ChatGPTSpecificSettings> {

    @Override
    public @Nullable ChatGPTSpecificSettings fromString(@NotNull String value) {
        String[] components = value.split(":");
        return new ChatGPTSpecificSettings(components[0], components[1]);
    }

    @Override
    public @Nullable String toString(@NotNull ChatGPTSpecificSettings value) {
        return value.getChatApiToken() + ":" + value.getSelectedModel();
    }
}
