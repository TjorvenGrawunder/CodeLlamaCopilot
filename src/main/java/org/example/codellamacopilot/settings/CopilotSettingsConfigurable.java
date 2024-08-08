package org.example.codellamacopilot.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class CopilotSettingsConfigurable implements Configurable {

    private CopilotSettingsComponent copilotSettingsComponent;


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Code Llama Copilot Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return copilotSettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        copilotSettingsComponent = new CopilotSettingsComponent();
        return copilotSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        CopilotSettings settings = CopilotSettings.getInstance();
        boolean modified = !copilotSettingsComponent.getCompletionAPITokenText().equals(settings.apiToken);
        modified |= !copilotSettingsComponent.getChatAPITokenText().equals(settings.chatApiToken);
        modified |= copilotSettingsComponent.getSelectedCompletionModel() != settings.usedModel;
        modified |= copilotSettingsComponent.getSelectedChatModel() != settings.usedChatModel;
        modified |= copilotSettingsComponent.getUseCompletion() != settings.useCompletion;
        return modified;
    }

    @Override
    public void apply() {
        CopilotSettings settings = CopilotSettings.getInstance();
        settings.apiToken = copilotSettingsComponent.getCompletionAPITokenText();
        settings.chatApiToken = copilotSettingsComponent.getChatAPITokenText();
        settings.usedModel = copilotSettingsComponent.getSelectedCompletionModel();
        settings.usedChatModel = copilotSettingsComponent.getSelectedChatModel();
        settings.useCompletion = copilotSettingsComponent.getUseCompletion();
    }

    @Override
    public void reset() {
        CopilotSettings settings = CopilotSettings.getInstance();
        copilotSettingsComponent.setCompletionAPITokenText(settings.apiToken);
        copilotSettingsComponent.setChatAPITokenText(settings.chatApiToken);
        copilotSettingsComponent.setSelectedModel(settings.usedModel);
        copilotSettingsComponent.setSelectedChatModel(settings.usedChatModel);
        copilotSettingsComponent.setUseCompletion(settings.useCompletion);
    }

    @Override
    public void disposeUIResources() {
        copilotSettingsComponent = null;
    }
}
