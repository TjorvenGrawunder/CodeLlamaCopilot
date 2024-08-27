package org.example.codellamacopilot.persistentconverter.settingsconverter.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.settings.modelsettings.SerializableSettingsParameters;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.CustomChatModelSpecificSettings;
import org.jetbrains.annotations.NotNull;

public class CustomChatModelSpecificSettingsConverter extends Converter<CustomChatModelSpecificSettings> {

    @Override
    public CustomChatModelSpecificSettings fromString(@NotNull String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SerializableSettingsParameters serializableSettingsParameters = objectMapper.readValue(value, SerializableSettingsParameters.class);
            return new CustomChatModelSpecificSettings(serializableSettingsParameters.getUrl(), serializableSettingsParameters.getApiToken(), serializableSettingsParameters.getSelectedModel());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(@NotNull CustomChatModelSpecificSettings value) {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializableSettingsParameters serializableSettingsParameters = new SerializableSettingsParameters(
                value.getUrl(), value.getChatApiToken(), value.getSelectedModel());
        try {
            return objectMapper.writeValueAsString(serializableSettingsParameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
