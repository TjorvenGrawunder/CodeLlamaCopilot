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
        boolean modified = !copilotSettingsComponent.getAPITokenText().equals(settings.apiToken);
        modified |= copilotSettingsComponent.getSelectedModel() != settings.usedModel;
        modified |= copilotSettingsComponent.getUseCompletion() != settings.useCompletion;
        return modified;
    }

    @Override
    public void apply() {
        CopilotSettingsState settings = CopilotSettingsState.getInstance();
        settings.apiToken = copilotSettingsComponent.getAPITokenText();
        settings.usedModel = copilotSettingsComponent.getSelectedModel();
        settings.useCompletion = copilotSettingsComponent.getUseCompletion();
    }

    @Override
    public void reset() {
        CopilotSettingsState settings = CopilotSettingsState.getInstance();
        copilotSettingsComponent.setAPITokenText(settings.apiToken);
        copilotSettingsComponent.getModelComboBox().setSelectedItem(settings.usedModel);
        copilotSettingsComponent.setUseCompletion(settings.useCompletion);
    }

    @Override
    public void disposeUIResources() {
        copilotSettingsComponent = null;
    }
}
