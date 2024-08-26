package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;

import javax.swing.*;

public abstract class CompletionModelSpecificSettings extends JPanel implements ModelSpecificSettingsIdentifier {
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
