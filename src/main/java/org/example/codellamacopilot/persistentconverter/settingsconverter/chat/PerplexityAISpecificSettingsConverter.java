package org.example.codellamacopilot.persistentconverter.settingsconverter.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.settings.modelsettings.SerializableSettingsParameters;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.CustomChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.PerplexityAISpecificSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerplexityAISpecificSettingsConverter extends Converter<PerplexityAISpecificSettings> {

    @Override
    public @Nullable PerplexityAISpecificSettings fromString(@NotNull String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SerializableSettingsParameters serializableSettingsParameters = objectMapper.readValue(value, SerializableSettingsParameters.class);
            return new PerplexityAISpecificSettings(serializableSettingsParameters.getApiToken(), serializableSettingsParameters.getSelectedModel());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable String toString(@NotNull PerplexityAISpecificSettings value) {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializableSettingsParameters serializableSettingsParameters = new SerializableSettingsParameters(
                null, value.getChatApiToken(), value.getSelectedModel());
        try {
            return objectMapper.writeValueAsString(serializableSettingsParameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
