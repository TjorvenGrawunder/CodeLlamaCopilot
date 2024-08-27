package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.llamaconnection.CustomModelRequestFormat;

import javax.swing.*;
import java.util.Locale;

public class CustomCompletionModelSpecificSettings extends CompletionModelSpecificSettings {
    private final JPasswordField customModelApiTokenTextField = new JPasswordField();
    private final JTextField urlTextField = new JTextField();
    private final JTextField modelTextField = new JTextField();
    private final JPanel PANEL;


    public CustomCompletionModelSpecificSettings() {

        PANEL = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Enter api url: "), urlTextField, 1, false)
                .addLabeledComponent(new JLabel("Enter your api token: "), customModelApiTokenTextField, 1, false)
                .addLabeledComponent(new JLabel("Enter model name: "), modelTextField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        this.add(PANEL);
    }

    public CustomCompletionModelSpecificSettings(String url, String apiToken, String model) {
        this();
        setUrl(url);
        setCompletionApiToken(apiToken);
        setSelectedModel(model);
    }

    @Override
    public CompletionRequestFormat getCompletionRequestFormat() {
        return new CustomModelRequestFormat(urlTextField.getText());
    }

    @Override
    public String getCompletionApiToken() {
        return String.valueOf(customModelApiTokenTextField.getPassword());
    }

    @Override
    public void setCompletionApiToken(String completionApiToken) {
        customModelApiTokenTextField.setText(completionApiToken);
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
    public boolean equals(CompletionModelSpecificSettings completionModelSpecificSettings){
        return this.getCompletionApiToken().equals(completionModelSpecificSettings.getCompletionApiToken()) &&
                this.getSelectedModel().equals(completionModelSpecificSettings.getSelectedModel()) &&
                this.getUrl().equals(((CustomCompletionModelSpecificSettings) completionModelSpecificSettings).getUrl());
    }
}
