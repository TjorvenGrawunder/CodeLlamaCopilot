package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import org.example.codellamacopilot.llamaconnection.RequestFormat;
import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;

import javax.swing.*;

public abstract class CompletionModelSpecificSettings extends JPanel implements ModelSpecificSettingsIdentifier {
    public abstract RequestFormat getCompletionRequestFormat();
    public abstract String getCompletionApiToken();
}
