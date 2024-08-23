package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;

import javax.swing.*;

public class ChatGPTSpecificSettings extends ChatModelSpecificSettings {
    private final JTextField chatGPTApiTokenTextField = new JTextField();
    private final ComboBox<String> chatModelComboBox = new ComboBox<>();
    private final JPanel PANEL;

    public ChatGPTSpecificSettings() {
        addModels();
        chatModelComboBox.setSelectedIndex(0);
        PANEL = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Enter chat gpt api token: "), chatGPTApiTokenTextField, 1, false)
                .addLabeledComponent(new JLabel("Select chat model: "), chatModelComboBox, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        this.add(PANEL);
    }

    @Override
    public ChatRequestFormat getChatRequestFormat() {
        ChatRequestFormat chatRequestFormat = new ChatGPTRequestFormat();
        chatRequestFormat.setModel((String) chatModelComboBox.getSelectedItem());
        return chatRequestFormat;
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
