package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;

import javax.swing.*;

/**
 * Specific Settings are settings that are specific to a particular completion model.
 */
public abstract class CompletionModelSpecificSettings extends JPanel implements ModelSpecificSettingsIdentifier {
    /**
     * Get the completion request format for the specific model.
     * @return the completion request format
     */
    public abstract CompletionRequestFormat getCompletionRequestFormat();
    public abstract String getCompletionApiToken();
    public abstract void setCompletionApiToken(String completionApiToken);
    public abstract String getSelectedModel();
    public abstract void setSelectedModel(String selectedModel);
    public abstract String getModelIdentifier();

    public boolean equals(CompletionModelSpecificSettings completionModelSpecificSettings){
        return this.getCompletionApiToken().equals(completionModelSpecificSettings.getCompletionApiToken()) && this.getSelectedModel().equals(completionModelSpecificSettings.getSelectedModel());
    }
}
