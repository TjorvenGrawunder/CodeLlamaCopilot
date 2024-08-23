package org.example.codellamacopilot.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBCardLayout;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CopilotSettingsComponent {

    //Components
    private final JPanel MAIN_PANEL;
    private final JPanel CHAT_SETTINGS_PANEL = new JPanel(new JBCardLayout());
    private final JPanel COMPLETION_SETTINGS_PANEL = new JPanel(new JBCardLayout());

    private final JBPasswordField HUGGING_FACE_API_TOKEN_TEXT_FIELD = new JBPasswordField();
    private final JBPasswordField CHAT_GPT_API_TOKEN_TEXT_FIELD = new JBPasswordField();

    private final ComboBox<CompletionRequestFormat> COMPLETION_MODEL_COMBO_BOX = new ComboBox<>();
    private final ComboBox<ChatRequestFormat> CHAT_MODEL_COMBO_BOX = new ComboBox<>();

    private final JBCheckBox USE_COMPLETION_CHECKBOX = new JBCheckBox("Use completion");

    //Completion Request Formats
    private final CompletionRequestFormat huggingFaceRequestFormat = new HuggingFaceRequestFormat();

    //Chat Request Formats
    private final ChatRequestFormat chatGPTRequestFormat = new ChatGPTRequestFormat();

    public CopilotSettingsComponent() {
        COMPLETION_MODEL_COMBO_BOX.addItem(huggingFaceRequestFormat);
        CHAT_MODEL_COMBO_BOX.addItem(chatGPTRequestFormat);
        COMPLETION_SETTINGS_PANEL.add(new HuggingFaceSpecificSettings(), huggingFaceRequestFormat.toString());
        CHAT_SETTINGS_PANEL.add(new ChatGPTSpecificSettings(), chatGPTRequestFormat.toString());

        COMPLETION_MODEL_COMBO_BOX.addItemListener(e -> {
            JBCardLayout cardLayout = (JBCardLayout) COMPLETION_SETTINGS_PANEL.getLayout();
            cardLayout.show(COMPLETION_SETTINGS_PANEL, COMPLETION_MODEL_COMBO_BOX.getSelectedItem().toString());
        });

        CHAT_MODEL_COMBO_BOX.addItemListener(e -> {
            JBCardLayout cardLayout = (JBCardLayout) CHAT_SETTINGS_PANEL.getLayout();
            cardLayout.show(CHAT_SETTINGS_PANEL, CHAT_MODEL_COMBO_BOX.getSelectedItem().toString());
        });
        /*MAIN_PANEL = FormBuilder.createFormBuilder()
                .addComponent(USE_COMPLETION_CHECKBOX, 1)
                .addLabeledComponent(new JBLabel("Enter huggingface api token: "), HUGGING_FACE_API_TOKEN_TEXT_FIELD, 1, false)
                .addLabeledComponent(new JBLabel("Enter chat gpt api token: "), CHAT_GPT_API_TOKEN_TEXT_FIELD, 1, false)
                .addLabeledComponent(new JBLabel("Select model: "), COMPLETION_MODEL_COMBO_BOX, 1, false)
                .addLabeledComponent(new JBLabel("Select chat model: "), CHAT_MODEL_COMBO_BOX, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();*/
        MAIN_PANEL = FormBuilder.createFormBuilder()
                .addComponent(USE_COMPLETION_CHECKBOX, 1)
                .addLabeledComponent(new JLabel("Choose Completion Model: "), COMPLETION_MODEL_COMBO_BOX, 1, false)
                .addComponent(COMPLETION_SETTINGS_PANEL, 1)
                .addLabeledComponent(new JLabel("Choose Chat Model: "), CHAT_MODEL_COMBO_BOX, 1, false)
                .addComponent(CHAT_SETTINGS_PANEL, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }


    public JPanel getPanel() {
        return MAIN_PANEL;
    }

    public JComponent getPreferredFocusedComponent() {
        return USE_COMPLETION_CHECKBOX;
    }

    public ComboBox<CompletionRequestFormat> getModelComboBox() {
        return COMPLETION_MODEL_COMBO_BOX;
    }

    public CompletionRequestFormat getSelectedCompletionModel() {
        //CompletionModelSpecificSettings completionModelSpecificSettings = (CompletionModelSpecificSettings) COMPLETION_SETTINGS_PANEL.getComponent(COMPLETION_MODEL_COMBO_BOX.getSelectedIndex());
        //RequestFormat completionRequestFormat = completionModelSpecificSettings.getCompletionRequestFormat();
        CompletionRequestFormat completionRequestFormat = (CompletionRequestFormat) COMPLETION_MODEL_COMBO_BOX.getSelectedItem();
        return completionRequestFormat;
    }

    public ChatRequestFormat getSelectedChatModel() {
        //ChatModelSpecificSettings chatModelSpecificSettings = (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        //ChatRequestFormat chatRequestFormat = chatModelSpecificSettings.getChatRequestFormat();
        ChatRequestFormat chatRequestFormat = (ChatRequestFormat) CHAT_MODEL_COMBO_BOX.getSelectedItem();
        chatRequestFormat.setModel(CHAT_MODEL_COMBO_BOX.getSelectedItem().toString());
        return chatRequestFormat;
    }

    @NotNull
    public String getCompletionAPITokenText() {
        return String.valueOf(HUGGING_FACE_API_TOKEN_TEXT_FIELD.getPassword());
    }

    public void setCompletionAPITokenText(@NotNull String newText) {
        HUGGING_FACE_API_TOKEN_TEXT_FIELD.setText(newText);
    }

    public String getChatAPITokenText() {
        return String.valueOf(CHAT_GPT_API_TOKEN_TEXT_FIELD.getPassword());
    }

    public void setChatAPITokenText(@NotNull String newText) {
        CHAT_GPT_API_TOKEN_TEXT_FIELD.setText(newText);
    }

    public void setSelectedModel(@NotNull CompletionRequestFormat model) {
        COMPLETION_MODEL_COMBO_BOX.setSelectedItem(model);
    }

    public void setSelectedChatModel(@NotNull ChatRequestFormat model) {
        String specificModel = CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex()).getName();
        CHAT_MODEL_COMBO_BOX.setSelectedItem(model);
    }

    public void setUseCompletion(boolean useCompletion) {
        USE_COMPLETION_CHECKBOX.setSelected(useCompletion);
    }

    public boolean getUseCompletion() {
        return USE_COMPLETION_CHECKBOX.isSelected();
    }
}
