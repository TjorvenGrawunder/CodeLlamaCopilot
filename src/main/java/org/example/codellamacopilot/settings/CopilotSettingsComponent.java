package org.example.codellamacopilot.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBCardLayout;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.PerplexityAIRequestFormat;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.CustomChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.PerplexityAISpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CustomCompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CopilotSettingsComponent {

    //Settings State
    CopilotSettingsState settingsInstance = CopilotSettingsState.getInstance();

    //Components
    private final JPanel MAIN_PANEL;
    private final JPanel CHAT_SETTINGS_PANEL = new JPanel(new JBCardLayout());
    private final JPanel COMPLETION_SETTINGS_PANEL = new JPanel(new JBCardLayout());

    private final ComboBox<String> COMPLETION_MODEL_COMBO_BOX = new ComboBox<>();
    private final ComboBox<String> CHAT_MODEL_COMBO_BOX = new ComboBox<>();

    private final JBCheckBox USE_COMPLETION_CHECKBOX = new JBCheckBox("Use completion");
    private final JBCheckBox USE_CHAT_AS_COMPLETION = new JBCheckBox("Use chat model as completion");

    //Specific Completion Settings
    CompletionModelSpecificSettings[] completionModelSpecificSettings = {settingsInstance.huggingFaceSpecificSettings, settingsInstance.customCompletionModelSpecificSettings};

    //Specific Chat Settings
    ChatModelSpecificSettings[] chatModelSpecificSettings = {settingsInstance.chatGPTSpecificSettings, settingsInstance.perplexityAISpecificSettings, settingsInstance.customChatModelSpecificSettings};

    private int selectedChatIndex = 0;
    private int selectedCompletionIndex = 0;

    public CopilotSettingsComponent() {
        for (CompletionModelSpecificSettings completionModelSpecificSetting : completionModelSpecificSettings) {
            COMPLETION_MODEL_COMBO_BOX.addItem(completionModelSpecificSetting.getModelIdentifier());
            COMPLETION_SETTINGS_PANEL.add(completionModelSpecificSetting, completionModelSpecificSetting.getModelIdentifier());
        }

        for (ChatModelSpecificSettings chatModelSpecificSetting : chatModelSpecificSettings) {
            CHAT_MODEL_COMBO_BOX.addItem(chatModelSpecificSetting.getModelIdentifier());
            CHAT_SETTINGS_PANEL.add(chatModelSpecificSetting, chatModelSpecificSetting.getModelIdentifier());
        }

        COMPLETION_MODEL_COMBO_BOX.addItemListener(e -> {
            JBCardLayout cardLayout = (JBCardLayout) COMPLETION_SETTINGS_PANEL.getLayout();
            selectedCompletionIndex = COMPLETION_MODEL_COMBO_BOX.getSelectedIndex();
            cardLayout.show(COMPLETION_SETTINGS_PANEL, COMPLETION_MODEL_COMBO_BOX.getSelectedItem().toString());
        });

        CHAT_MODEL_COMBO_BOX.addItemListener(e -> {
            JBCardLayout cardLayout = (JBCardLayout) CHAT_SETTINGS_PANEL.getLayout();
            selectedChatIndex = CHAT_MODEL_COMBO_BOX.getSelectedIndex();
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
                .addComponent(USE_CHAT_AS_COMPLETION, 1)
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

    public ComboBox<String> getModelComboBox() {
        return COMPLETION_MODEL_COMBO_BOX;
    }

    public String getSelectedCompletionModel() {
        //CompletionModelSpecificSettings completionModelSpecificSettings = (CompletionModelSpecificSettings) COMPLETION_SETTINGS_PANEL.getComponent(COMPLETION_MODEL_COMBO_BOX.getSelectedIndex());
        //RequestFormat completionRequestFormat = completionModelSpecificSettings.getCompletionRequestFormat();
        return (String) COMPLETION_MODEL_COMBO_BOX.getSelectedItem();
    }

    public String getSelectedChatModel() {
        //ChatModelSpecificSettings chatModelSpecificSettings = (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        //ChatRequestFormat chatRequestFormat = chatModelSpecificSettings.getChatRequestFormat();
        return (String) CHAT_MODEL_COMBO_BOX.getSelectedItem();
    }

    @NotNull
    public String getCompletionAPITokenText() {
        CompletionModelSpecificSettings completionModelSpecificSettings = (CompletionModelSpecificSettings) COMPLETION_SETTINGS_PANEL.getComponent(COMPLETION_MODEL_COMBO_BOX.getSelectedIndex());
        //this.completionModelSpecificSettings[COMPLETION_MODEL_COMBO_BOX.getSelectedIndex()] = completionModelSpecificSettings;
        return completionModelSpecificSettings.getCompletionApiToken();
    }


    public String getChatAPITokenText() {
        ChatModelSpecificSettings chatModelSpecificSettings = (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        //this.chatModelSpecificSettings[CHAT_MODEL_COMBO_BOX.getSelectedIndex()] = chatModelSpecificSettings;
        return chatModelSpecificSettings.getChatApiToken();
    }

    public void setSelectedModel(@NotNull String model) {
        COMPLETION_MODEL_COMBO_BOX.setSelectedItem(model);
    }

    public void setSelectedChatModel(@NotNull String model) {
        CHAT_MODEL_COMBO_BOX.setSelectedItem(model);
    }

    public void setUseCompletion(boolean useCompletion) {
        USE_COMPLETION_CHECKBOX.setSelected(useCompletion);
    }

    public boolean getUseCompletion() {
        return USE_COMPLETION_CHECKBOX.isSelected();
    }

    public void setUseChatAsCompletion(boolean useChatAsCompletion) {
        USE_CHAT_AS_COMPLETION.setSelected(useChatAsCompletion);
    }

    public boolean getUseChatAsCompletion() {
        return USE_CHAT_AS_COMPLETION.isSelected();
    }

    public ChatGPTSpecificSettings getChatGPTSpecificSettings() {
        chatModelSpecificSettings[CHAT_MODEL_COMBO_BOX.getSelectedIndex()] =
                (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        return (ChatGPTSpecificSettings) chatModelSpecificSettings[0];
    }

    public void setChatGPTSpecificSettings(ChatGPTSpecificSettings chatGPTSpecificSettings) {
        this.chatModelSpecificSettings[0] = chatGPTSpecificSettings;
    }

    public PerplexityAISpecificSettings getPerplexityAISpecificSettings() {
        chatModelSpecificSettings[CHAT_MODEL_COMBO_BOX.getSelectedIndex()] =
                (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        return (PerplexityAISpecificSettings) chatModelSpecificSettings[1];
    }

    public void setPerplexityAISpecificSettings(PerplexityAISpecificSettings perplexityAISpecificSettings) {
        this.chatModelSpecificSettings[1] = perplexityAISpecificSettings;
    }

    public CustomChatModelSpecificSettings getCustomChatModelSpecificSettings() {
        chatModelSpecificSettings[CHAT_MODEL_COMBO_BOX.getSelectedIndex()] =
                (ChatModelSpecificSettings) CHAT_SETTINGS_PANEL.getComponent(CHAT_MODEL_COMBO_BOX.getSelectedIndex());
        return (CustomChatModelSpecificSettings) chatModelSpecificSettings[2];
    }

    public void setCustomChatModelSpecificSettings(CustomChatModelSpecificSettings customChatModelSpecificSettings) {
        this.chatModelSpecificSettings[2] = customChatModelSpecificSettings;
    }

    public HuggingFaceSpecificSettings getHuggingFaceSpecificSettings() {
        completionModelSpecificSettings[COMPLETION_MODEL_COMBO_BOX.getSelectedIndex()] =
                (CompletionModelSpecificSettings) COMPLETION_SETTINGS_PANEL
                        .getComponent(COMPLETION_MODEL_COMBO_BOX.getSelectedIndex());
        return (HuggingFaceSpecificSettings) completionModelSpecificSettings[0];
    }

    public void setHuggingFaceSpecificSettings(HuggingFaceSpecificSettings huggingFaceSpecificSettings) {
        this.completionModelSpecificSettings[0] = huggingFaceSpecificSettings;
    }

    public CustomCompletionModelSpecificSettings getCustomCompletionModelSpecificSettings() {
        completionModelSpecificSettings[COMPLETION_MODEL_COMBO_BOX.getSelectedIndex()] =
                (CompletionModelSpecificSettings) COMPLETION_SETTINGS_PANEL
                        .getComponent(COMPLETION_MODEL_COMBO_BOX.getSelectedIndex());
        return (CustomCompletionModelSpecificSettings) completionModelSpecificSettings[1];
    }

    public void setCustomCompletionModelSpecificSettings(CustomCompletionModelSpecificSettings customCompletionModelSpecificSettings) {
        this.completionModelSpecificSettings[1] = customCompletionModelSpecificSettings;
    }
}
