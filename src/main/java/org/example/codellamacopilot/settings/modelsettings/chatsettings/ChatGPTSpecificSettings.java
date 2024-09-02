package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;

import javax.swing.*;

public class ChatGPTSpecificSettings extends ChatModelSpecificSettings {
    private final JPasswordField chatGPTApiTokenTextField = new JPasswordField();
    private final ComboBox<String> chatModelComboBox = new ComboBox<>();
    private final JPanel PANEL;

    private String chatApiToken;

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

    public ChatGPTSpecificSettings(String chatApiToken, String selectedModel){
        this();
        setChatApiToken(chatApiToken);
        setSelectedModel(selectedModel);
    }

    @Override
    public ChatRequestFormat getChatRequestFormat(boolean persistentChatHistory) {
        return new ChatGPTRequestFormat((String) chatModelComboBox.getSelectedItem(), persistentChatHistory);
    }

    @Override
    public String getChatApiToken() {
        return String.valueOf(chatGPTApiTokenTextField.getPassword());
    }

    @Override
    public void setChatApiToken(String chatApiToken) {
        chatGPTApiTokenTextField.setText(chatApiToken);
    }

    @Override
    public String getSelectedModel() {
        return (String) chatModelComboBox.getSelectedItem();
    }

    @Override
    public void setSelectedModel(String selectedModel) {
        chatModelComboBox.setSelectedItem(selectedModel);
    }

    @Override
    public String getModelIdentifier() {
        return "ChatGPT";
    }

    private void addModels(){
        chatModelComboBox.addItem("gpt-3.5-turbo");
        chatModelComboBox.addItem("gpt-4o-mini");
        chatModelComboBox.addItem("gpt-4o");
        chatModelComboBox.addItem("gpt-4-turbo");
        chatModelComboBox.addItem("gpt-4");
    }
}
