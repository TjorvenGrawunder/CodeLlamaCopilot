package org.example.codellamacopilot.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.persistentconverter.settingsconverter.chat.ChatGPTSpecificSettingsConverter;
import org.example.codellamacopilot.persistentconverter.settingsconverter.chat.CustomChatModelSpecificSettingsConverter;
import org.example.codellamacopilot.persistentconverter.settingsconverter.chat.PerplexityAISpecificSettingsConverter;
import org.example.codellamacopilot.persistentconverter.settingsconverter.completion.CustomCompletionModelSpecificSettingsConverter;
import org.example.codellamacopilot.persistentconverter.settingsconverter.completion.HuggingFaceSpecificSettingsConverter;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.CustomChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.PerplexityAISpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CustomCompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to store the settings of the plugin.
 */
@State(
        name = "org.example.codellamacopilot.settings.CopilotSettingsState",
        storages = @Storage("CodeLlamaCopilotSettings.xml")
)
public final class CopilotSettingsState implements PersistentStateComponent<CopilotSettingsState> {

    //Used Models
    public String usedModel;
    public String usedChatModel;

    //Specific Chat Settings
    @OptionTag(converter = ChatGPTSpecificSettingsConverter.class)
    public ChatGPTSpecificSettings chatGPTSpecificSettings;
    @OptionTag(converter = PerplexityAISpecificSettingsConverter.class)
    public PerplexityAISpecificSettings perplexityAISpecificSettings;
    @OptionTag(converter = CustomChatModelSpecificSettingsConverter.class)
    public CustomChatModelSpecificSettings customChatModelSpecificSettings;
    //Specific Completion Settings
    @OptionTag(converter = HuggingFaceSpecificSettingsConverter.class)
    public HuggingFaceSpecificSettings huggingFaceSpecificSettings;
    @OptionTag(converter = CustomCompletionModelSpecificSettingsConverter.class)
    public CustomCompletionModelSpecificSettings customCompletionModelSpecificSettings;

    //Current API Tokens
    public String apiToken;
    public String chatApiToken;

    public boolean useCompletion;
    public boolean useChatAsCompletion;

    public CopilotSettingsState() {
        usedModel = "huggingFaceRequestFormat";
        usedChatModel = "chatGPTRequestFormat";
        chatGPTSpecificSettings = new ChatGPTSpecificSettings();
        perplexityAISpecificSettings = new PerplexityAISpecificSettings();
        customChatModelSpecificSettings = new CustomChatModelSpecificSettings();
        huggingFaceSpecificSettings = new HuggingFaceSpecificSettings();
        customCompletionModelSpecificSettings = new CustomCompletionModelSpecificSettings();
        apiToken = "";
        chatApiToken = "";
        useCompletion = false;
        useChatAsCompletion = false;
    }

    public static CopilotSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CopilotSettingsState.class);
    }
    @Override
    public CopilotSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CopilotSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * Get the completion request format for the used model from string.
     * @return the completion request format
     */
    public CompletionRequestFormat getUsedCompletionRequestFormat() {
        return switch (usedModel) {
            case "HuggingFace" -> huggingFaceSpecificSettings.getCompletionRequestFormat();
            case "Custom" -> customCompletionModelSpecificSettings.getCompletionRequestFormat();
            default -> null;
        };
    }

    /**
     * Get the chat request format for the used chat model from string.
     * @param persistentChatHistory whether the chat history should be persistent or not
     * @return the chat request format
     */
    public ChatRequestFormat getUsedChatRequestFormat(boolean persistentChatHistory) {
        return switch (usedChatModel) {
            case "ChatGPT" -> chatGPTSpecificSettings.getChatRequestFormat(persistentChatHistory);
            case "PerplexityAI" -> perplexityAISpecificSettings.getChatRequestFormat(persistentChatHistory);
            case "Custom" -> customChatModelSpecificSettings.getChatRequestFormat(persistentChatHistory);
            default -> null;
        };
    }
}
