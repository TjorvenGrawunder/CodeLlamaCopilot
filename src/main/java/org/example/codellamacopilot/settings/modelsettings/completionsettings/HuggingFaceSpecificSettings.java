package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import com.intellij.openapi.ui.ComboBox;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;

import javax.swing.*;

public class HuggingFaceSpecificSettings extends CompletionModelSpecificSettings {
    private final JTextField huggingFaceApiTokenTextField = new JTextField();
    private final ComboBox<String> completionModelComboBox = new ComboBox<>();

    public HuggingFaceSpecificSettings() {
        super();
        addModels();
        completionModelComboBox.setSelectedIndex(0);
        this.add(new JLabel("Select model: "));
        this.add(completionModelComboBox);
        this.add(new JLabel("Enter hugging face api token: "));
        this.add(huggingFaceApiTokenTextField);
    }

    @Override
    public CompletionRequestFormat getCompletionRequestFormat() {
        return new HuggingFaceRequestFormat(completionModelComboBox.getSelectedItem().toString());
    }

    @Override
    public String getCompletionApiToken() {
        return huggingFaceApiTokenTextField.getText();
    }

    private void addModels(){
        completionModelComboBox.addItem("codellama/CodeLlama-7b-hf");
        completionModelComboBox.addItem("codellama/CodeLlama-13b-hf");
        completionModelComboBox.addItem("bigcode/starcoder");
    }

}
