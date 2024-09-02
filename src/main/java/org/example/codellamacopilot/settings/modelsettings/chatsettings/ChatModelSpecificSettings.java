package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class ChatModelSpecificSettings extends JPanel implements ModelSpecificSettingsIdentifier {
    public abstract ChatRequestFormat getChatRequestFormat(boolean persistentChatHistory);
    public abstract String getChatApiToken();
    public abstract void setChatApiToken(String chatApiToken);
    public abstract String getSelectedModel();
    public abstract void setSelectedModel(String selectedModel);
    public abstract String getModelIdentifier();

    public boolean equals(@NotNull ChatModelSpecificSettings chatModelSpecificSettings){
        return this.getChatApiToken().equals(chatModelSpecificSettings.getChatApiToken()) && this.getSelectedModel().equals(chatModelSpecificSettings.getSelectedModel());
    }
}
