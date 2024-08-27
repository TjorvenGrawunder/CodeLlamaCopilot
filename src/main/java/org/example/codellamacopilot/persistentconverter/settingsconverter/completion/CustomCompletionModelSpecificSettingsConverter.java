package org.example.codellamacopilot.persistentconverter.settingsconverter.completion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.settings.modelsettings.SerializableSettingsParameters;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.CustomChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CustomCompletionModelSpecificSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomCompletionModelSpecificSettingsConverter extends Converter<CustomCompletionModelSpecificSettings> {
    @Override
    public @Nullable CustomCompletionModelSpecificSettings fromString(@NotNull String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SerializableSettingsParameters serializableSettingsParameters = objectMapper.readValue(value, SerializableSettingsParameters.class);
            return new CustomCompletionModelSpecificSettings(serializableSettingsParameters.getUrl(), serializableSettingsParameters.getApiToken(), serializableSettingsParameters.getSelectedModel());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable String toString(@NotNull CustomCompletionModelSpecificSettings value) {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializableSettingsParameters serializableSettingsParameters = new SerializableSettingsParameters(
                value.getUrl(), value.getCompletionApiToken(), value.getSelectedModel());
        try {
            return objectMapper.writeValueAsString(serializableSettingsParameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
