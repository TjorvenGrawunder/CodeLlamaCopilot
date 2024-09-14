package org.example.codellamacopilot.persistentconverter.settingsconverter.completion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.persistentconverter.settingsconverter.SettingsConverter;
import org.example.codellamacopilot.settings.modelsettings.SerializableSettingsParameters;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * {@inheritDoc}.
 */
public class HuggingFaceSpecificSettingsConverter extends Converter<HuggingFaceSpecificSettings> implements SettingsConverter {

    @Override
    public @Nullable HuggingFaceSpecificSettings fromString(@NotNull String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SerializableSettingsParameters serializableSettingsParameters = objectMapper.readValue(value, SerializableSettingsParameters.class);
            return new HuggingFaceSpecificSettings(serializableSettingsParameters.getApiToken(), serializableSettingsParameters.getSelectedModel());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable String toString(@NotNull HuggingFaceSpecificSettings value) {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializableSettingsParameters serializableSettingsParameters = new SerializableSettingsParameters(
                null, value.getCompletionApiToken(), value.getSelectedModel());
        try {
            return objectMapper.writeValueAsString(serializableSettingsParameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
