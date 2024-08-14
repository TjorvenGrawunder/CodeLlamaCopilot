package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.ui.components.JBPanel;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;

import javax.swing.*;

public abstract class ChatModelSpecificSettings extends JPanel implements ModelSpecificSettingsIdentifier {
    public abstract ChatRequestFormat getChatRequestFormat();
    public abstract String getChatApiToken();
}
