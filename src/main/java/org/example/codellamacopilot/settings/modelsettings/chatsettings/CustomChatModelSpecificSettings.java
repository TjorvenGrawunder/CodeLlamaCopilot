package org.example.codellamacopilot.settings.modelsettings.chatsettings;

import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.CustomChatRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CustomCompletionModelSpecificSettings;

import javax.swing.*;

public class CustomChatModelSpecificSettings extends ChatModelSpecificSettings{

    private final JTextField urlTextField = new JTextField();
    private final JPasswordField customModelApiTokenTextField = new JPasswordField();
    private final JTextField modelTextField = new JTextField();
    private final JPanel PANEL;

    public CustomChatModelSpecificSettings() {
        PANEL = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Enter api url: "), urlTextField, 1, false)
                .addLabeledComponent(new JLabel("Enter your api token: "), customModelApiTokenTextField, 1, false)
                .addLabeledComponent(new JLabel("Enter model name: "), modelTextField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        this.add(PANEL);
    }

    public CustomChatModelSpecificSettings(String url, String chatApiToken, String selectedModel){
        this();
        setUrl(url);
        setChatApiToken(chatApiToken);
        setSelectedModel(selectedModel);
    }

    @Override
    public ChatRequestFormat getChatRequestFormat() {
        return new CustomChatRequestFormat(urlTextField.getText(), modelTextField.getText());
    }

    @Override
    public String getChatApiToken() {
        return String.valueOf(customModelApiTokenTextField.getPassword());
    }

    @Override
    public void setChatApiToken(String chatApiToken) {
        this.customModelApiTokenTextField.setText(chatApiToken);
    }

    @Override
    public String getSelectedModel() {
        return modelTextField.getText();
    }

    @Override
    public void setSelectedModel(String selectedModel) {
        modelTextField.setText(selectedModel);
    }

    public String getUrl() {
        return urlTextField.getText();
    }

    public void setUrl(String url) {
        urlTextField.setText(url);
    }

    @Override
    public String getModelIdentifier() {
        return "Custom";
    }

    @Override
    public boolean equals(ChatModelSpecificSettings chatModelSpecificSettings){
        return this.getChatApiToken().equals(chatModelSpecificSettings.getChatApiToken()) &&
                this.getSelectedModel().equals(chatModelSpecificSettings.getSelectedModel()) &&
                this.getUrl().equals(((CustomChatModelSpecificSettings) chatModelSpecificSettings).getUrl());
    }
}
