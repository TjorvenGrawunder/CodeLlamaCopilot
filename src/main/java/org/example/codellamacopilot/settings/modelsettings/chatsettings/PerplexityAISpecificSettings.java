package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.PerplexityAIRequestFormat;

import javax.swing.*;

public class PerplexityAISpecificSettings extends ChatModelSpecificSettings {
    private final JPasswordField perplexityApiTokenTextField = new JPasswordField();
    private final ComboBox<String> chatModelComboBox = new ComboBox<>();
    private final JPanel PANEL;

    public PerplexityAISpecificSettings() {
        addModels();
        chatModelComboBox.setSelectedIndex(0);
        PANEL = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Enter perplexity api token: "), perplexityApiTokenTextField, 1, false)
                .addLabeledComponent(new JLabel("Select chat model: "), chatModelComboBox, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        this.add(PANEL);
    }

    public PerplexityAISpecificSettings(String chatApiToken, String selectedModel){
        this();
        setChatApiToken(chatApiToken);
        setSelectedModel(selectedModel);
    }

    @Override
    public ChatRequestFormat getChatRequestFormat() {
        return new PerplexityAIRequestFormat((String) chatModelComboBox.getSelectedItem());
    }

    @Override
    public String getChatApiToken() {
        return String.valueOf(perplexityApiTokenTextField.getPassword());
    }

    @Override
    public void setChatApiToken(String chatApiToken) {
        perplexityApiTokenTextField.setText(chatApiToken);
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
        return "PerplexityAI";
    }

    private void addModels(){
        chatModelComboBox.addItem("llama-3.1-sonar-small-128k-chat");
        chatModelComboBox.addItem("llama-3.1-sonar-small-128k-online");
        chatModelComboBox.addItem("llama-3.1-sonar-large-128k-chat");
        chatModelComboBox.addItem("llama-3.1-sonar-large-128k-online");
        chatModelComboBox.addItem("llama-3.1-8b-instruct");
        chatModelComboBox.addItem("llama-3.1-70b-instruct");
    }
}
