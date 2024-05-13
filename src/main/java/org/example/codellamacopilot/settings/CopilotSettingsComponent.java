package org.example.codellamacopilot.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.RequestFormat;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class CopilotSettingsComponent {

    private final JPanel MAIN_PANEL;
    private final JBPasswordField API_TOKEN_TEXT_FIELD = new JBPasswordField();
    private final ComboBox<RequestFormat> MODEL_COMBO_BOX = new ComboBox<>();
    private final JBCheckBox USE_COMPLETION_CHECKBOX = new JBCheckBox("Use completion");

    private final RequestFormat huggingFaceRequestFormat = new HuggingFaceRequestFormat();

    public CopilotSettingsComponent() {
        MODEL_COMBO_BOX.addItem(huggingFaceRequestFormat);
        MAIN_PANEL = FormBuilder.createFormBuilder()
                .addComponent(USE_COMPLETION_CHECKBOX, 1)
                .addLabeledComponent(new JBLabel("Enter huggingface api token: "), API_TOKEN_TEXT_FIELD, 1, false)
                .addLabeledComponent(new JBLabel("Select model: "), MODEL_COMBO_BOX, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }


    public JPanel getPanel() {
        return MAIN_PANEL;
    }

    public JComponent getPreferredFocusedComponent() {
        return API_TOKEN_TEXT_FIELD;
    }

    public ComboBox<RequestFormat> getModelComboBox() {
        return MODEL_COMBO_BOX;
    }

    public RequestFormat getSelectedModel() {
        return MODEL_COMBO_BOX.getItemAt(MODEL_COMBO_BOX.getSelectedIndex());
    }

    @NotNull
    public String getAPITokenText() {
        return String.valueOf(API_TOKEN_TEXT_FIELD.getPassword());
    }

    public void setAPITokenText(@NotNull String newText) {
        API_TOKEN_TEXT_FIELD.setText(newText);
    }

    public void setSelectedModel(@NotNull RequestFormat model) {
        MODEL_COMBO_BOX.setSelectedItem(model);
    }

    public void setUseCompletion(boolean useCompletion) {
        USE_COMPLETION_CHECKBOX.setSelected(useCompletion);
    }

    public boolean getUseCompletion() {
        return USE_COMPLETION_CHECKBOX.isSelected();
    }
}
