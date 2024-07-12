package org.example.codellamacopilot.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class CopilotSettingsConfigurable implements Configurable {

    private CopilotSettingsComponent copilotSettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered in an applicationConfigurable EP

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
        CopilotSettingsState settings = CopilotSettingsState.getInstance();
        boolean modified = !copilotSettingsComponent.getCompletionAPITokenText().equals(settings.apiToken);
        modified |= !copilotSettingsComponent.getChatAPITokenText().equals(settings.chatApiToken);
        modified |= copilotSettingsComponent.getSelectedCompletionModel() != settings.usedModel;
        modified |= copilotSettingsComponent.getUseCompletion() != settings.useCompletion;
        return modified;
    }

    @Override
    public void apply() {
        CopilotSettingsState settings = CopilotSettingsState.getInstance();
        settings.apiToken = copilotSettingsComponent.getCompletionAPITokenText();
        settings.chatApiToken = copilotSettingsComponent.getChatAPITokenText();
        settings.usedModel = copilotSettingsComponent.getSelectedCompletionModel();
        settings.useCompletion = copilotSettingsComponent.getUseCompletion();
    }

    @Override
    public void reset() {
        CopilotSettingsState settings = CopilotSettingsState.getInstance();
        copilotSettingsComponent.setCompletionAPITokenText(settings.apiToken);
        copilotSettingsComponent.setChatAPITokenText(settings.chatApiToken);
        copilotSettingsComponent.getModelComboBox().setSelectedItem(settings.usedModel);
        copilotSettingsComponent.setUseCompletion(settings.useCompletion);
    }

    @Override
    public void disposeUIResources() {
        copilotSettingsComponent = null;
    }
}
