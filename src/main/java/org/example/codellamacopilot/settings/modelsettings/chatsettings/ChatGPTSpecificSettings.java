package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBPanel;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;

import javax.swing.*;

public class ChatGPTSpecificSettings extends ChatModelSpecificSettings {
    private final JTextField chatGPTApiTokenTextField = new JTextField();
    private final ComboBox<String> chatModelComboBox = new ComboBox<>();

    public ChatGPTSpecificSettings() {
        super();
        addModels();
        this.add(new JLabel("Enter chat gpt api token: "));
        this.add(chatGPTApiTokenTextField);
        this.add(new JLabel("Select chat model: "));
        this.add(chatModelComboBox);
    }

    @Override
    public ChatRequestFormat getChatRequestFormat() {
        return new ChatGPTRequestFormat();
    }

    @Override
    public String getChatApiToken() {
        return chatGPTApiTokenTextField.getText();
    }

    private void addModels(){
        chatModelComboBox.addItem("gpt-3.5-turbo");
        chatModelComboBox.addItem("gpt-4o-mini");
        chatModelComboBox.addItem("gpt-4o");
        chatModelComboBox.addItem("gpt-4-turbo");
        chatModelComboBox.addItem("gpt-4");
    }
}
