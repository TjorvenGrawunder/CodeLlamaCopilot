package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;

import javax.swing.*;

public class HuggingFaceSpecificSettings extends CompletionModelSpecificSettings {
    private final JPasswordField huggingFaceApiTokenTextField = new JPasswordField();
    private final ComboBox<String> completionModelComboBox = new ComboBox<>();
    private final JPanel PANEL;

    public HuggingFaceSpecificSettings() {
        super();
        addModels();
        completionModelComboBox.setSelectedIndex(0);
        PANEL = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Enter hugging face api token: "), huggingFaceApiTokenTextField, 1, false)
                .addLabeledComponent(new JLabel("Select model: "), completionModelComboBox, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        this.add(PANEL);
    }

    public HuggingFaceSpecificSettings(String completionApiToken, String selectedModel){
        this();
        setCompletionApiToken(completionApiToken);
        setSelectedModel(selectedModel);
    }

    @Override
    public CompletionRequestFormat getCompletionRequestFormat() {
        return new HuggingFaceRequestFormat( (String) completionModelComboBox.getSelectedItem());
    }

    @Override
    public String getCompletionApiToken() {
        return String.valueOf(huggingFaceApiTokenTextField.getPassword());
    }

    @Override
    public void setCompletionApiToken(String completionApiToken) {
        huggingFaceApiTokenTextField.setText(completionApiToken);
    }

    @Override
    public String getSelectedModel() {
        return (String) completionModelComboBox.getSelectedItem();
    }

    @Override
    public void setSelectedModel(String selectedModel) {
        completionModelComboBox.setSelectedItem(selectedModel);
    }

    @Override
    public String getModelIdentifier() {
        return "HuggingFace";
    }

    private void addModels(){
        completionModelComboBox.addItem("codellama/CodeLlama-7b-hf");
        completionModelComboBox.addItem("codellama/CodeLlama-13b-hf");
        completionModelComboBox.addItem("meta-llama/CodeLlama-7b-hf");
        completionModelComboBox.addItem("meta-llama/CodeLlama-13b-hf");
        completionModelComboBox.addItem("bigcode/starcoder");
    }

}
