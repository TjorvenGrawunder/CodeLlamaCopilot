package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HuggingFaceSpecificSettingsConverter extends Converter<HuggingFaceSpecificSettings> {

    @Override
    public @Nullable HuggingFaceSpecificSettings fromString(@NotNull String value) {
        String[] components = value.split(":");
        return new HuggingFaceSpecificSettings(components[0], components[1]);
    }

    @Override
    public @Nullable String toString(@NotNull HuggingFaceSpecificSettings value) {
        return value.getCompletionApiToken() + ":" + value.getSelectedModel();
    }
}
