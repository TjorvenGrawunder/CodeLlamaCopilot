package org.example.codellamacopilot.settings.modelsettings.completionsettings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;

import javax.swing.*;

public class HuggingFaceSpecificSettings extends CompletionModelSpecificSettings {
    private final JTextField huggingFaceApiTokenTextField = new JTextField();
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
