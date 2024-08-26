package org.example.codellamacopilot.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.example.codellamacopilot.chatwindow.requestformats.ChatRequestFormat;
import org.example.codellamacopilot.chatwindow.requestformats.PerplexityAIRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.example.codellamacopilot.persistentconverter.*;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatGPTSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.ChatModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.chatsettings.PerplexityAISpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.CompletionModelSpecificSettings;
import org.example.codellamacopilot.settings.modelsettings.completionsettings.HuggingFaceSpecificSettings;
import org.jetbrains.annotations.NotNull;


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
    //Specific Completion Settings
    @OptionTag(converter = HuggingFaceSpecificSettingsConverter.class)
    public HuggingFaceSpecificSettings huggingFaceSpecificSettings;

    //Current API Tokens
    public String apiToken;
    public String chatApiToken;

    public boolean useCompletion;

    public CopilotSettingsState() {
        usedModel = "huggingFaceRequestFormat";
        usedChatModel = "chatGPTRequestFormat";
        chatGPTSpecificSettings = new ChatGPTSpecificSettings();
        perplexityAISpecificSettings = new PerplexityAISpecificSettings();
        huggingFaceSpecificSettings = new HuggingFaceSpecificSettings();
        apiToken = "";
        chatApiToken = "";
        useCompletion = false;
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

    public CompletionRequestFormat getUsedCompletionRequestFormat() {
        return switch (usedModel) {
            case "HuggingFace" -> huggingFaceSpecificSettings.getCompletionRequestFormat();
            default -> null;
        };
    }

    public ChatRequestFormat getUsedChatRequestFormat() {
        return switch (usedChatModel) {
            case "ChatGPT" -> chatGPTSpecificSettings.getChatRequestFormat();
            case "PerplexityAI" -> perplexityAISpecificSettings.getChatRequestFormat();
            default -> null;
        };
    }
}
