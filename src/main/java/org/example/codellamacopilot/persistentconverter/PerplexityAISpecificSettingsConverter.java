package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.PerplexityAISpecificSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerplexityAISpecificSettingsConverter extends Converter<PerplexityAISpecificSettings> {

    @Override
    public @Nullable PerplexityAISpecificSettings fromString(@NotNull String value) {
        String[] components = value.split(":");
        return new PerplexityAISpecificSettings(components[0], components[1]);
    }

    @Override
    public @Nullable String toString(@NotNull PerplexityAISpecificSettings value) {
        return value.getChatApiToken() + ":" + value.getSelectedModel();
    }
}
