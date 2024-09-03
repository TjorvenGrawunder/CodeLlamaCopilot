package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.google.common.base.Throwables;
import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.UISettingsListener;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;

import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextComponent;

import com.intellij.util.messages.MessageBus;
import org.example.codellamacopilot.chatwindow.api.ChatClient;

import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistoryManipulator;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.dialogs.StackTraceDialogWrapper;
import org.example.codellamacopilot.exceptions.ErrorMessageException;
import org.example.codellamacopilot.icons.LLMCopilotIcons;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class ChatWindow {
    private JPanel mainPanel;
    private JPanel messagePanel;
    private JBScrollPane scrollPane;
    private ExpandableTextField inputField;
    private ChatClient chatClient;
    private Project project;
    private ExtendableTextComponent.Extension sendExtension;

    public ChatWindow(Project project) {
        this.project = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        bus.connect().subscribe(UISettingsListener.TOPIC, (UISettingsListener) this::reloadChatMessages);

        this.chatClient = new ChatClient(this.project, CopilotSettingsState.getInstance().getUsedChatRequestFormat(true), true);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        scrollPane = new JBScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new ExpandableTextField();

        boolean isDark = EditorColorsManager.getInstance().isDarkEditor();
        sendExtension = ExtendableTextComponent.Extension.create(
                isDark ? LLMCopilotIcons.SendIconDark: LLMCopilotIcons.SendIconLight, "Send message", this::onSendClick);

        inputField.addExtension(sendExtension);

        inputField.addActionListener(e -> {
            onSendClick();
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
        initChatMessages();
    }

    public JComponent getContent() {
        return mainPanel;
    }

    private void sendMessage(String message) {
        ChatElement chatElement = new ChatElement(message);
        messagePanel.add(chatElement);
        messagePanel.revalidate();

        //Scroll to the bottom of the chat
        ApplicationManager.getApplication().invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        boolean skipResponse = false;
        String response;
        try {
            switch (message) {
                case "\\debug" -> response = chatClient.debug();
                case "\\explain" -> response = chatClient.explain();
                case "\\test" -> {
                    response = chatClient.test();
                    skipResponse = true;
                }
                case "\\clear" -> {
                    messagePanel.removeAll();
                    ChatHistoryManipulator chatHistory = new ChatHistoryManipulator();
                    chatHistory.clearChatHistory();
                    response = "Chat cleared!";
                    skipResponse = true;
                }
                default -> response = chatClient.sendMessage(message);
            }

            if(!skipResponse){
                String[] messageParts = response.split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|js|lua|make|markdown|php|python|r|sql|tex|text|xml|groovy))|```");

                ApplicationManager.getApplication().invokeLater(() -> {
                    messagePanel.add(new ChatResponseField(messageParts, project, this));
                    messagePanel.revalidate();
                });
            }
        }catch (ErrorMessageException errorMessageException){
            chatClient.getRequestFormat().removeLastMessage();
            sendChatAlert(errorMessageException.getMessage(), "");
        } catch (Exception e) {
            chatClient.getRequestFormat().removeLastMessage();
            String stackTrace = Throwables.getStackTraceAsString(e);

            sendChatAlert("An error occurred while sending the message. Please try again.", stackTrace);
        }
    }

    private void initChatMessages() {
        ChatHistoryManipulator chatHistoryManipulator = new ChatHistoryManipulator();
        if (chatHistoryManipulator.getMessages().size() == 1 + chatHistoryManipulator.getContextCounter()) {
            // Initialize chat messages
            ChatElement chatElement = new ChatElement("Hello! I'm your personal programming assistant. How can I help you today?");
            messagePanel.add(chatElement);
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            for (MessageObject message : chatHistoryManipulator.getMessages()) {
                if (message.getRole().equals("user")) {
                    ChatElement chatElement = new ChatElement(message.getContent());
                    messagePanel.add(chatElement);
                } else if (message.getRole().equals("assistant")) {
                    String[] messageParts = message.getContent().split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|js|lua|make|markdown|php|python|r|sql|tex|text|xml|groovy))|```");
                    ChatResponseField chatResponseField = new ChatResponseField(messageParts, project, this);
                    messagePanel.add(chatResponseField);
                }
                messagePanel.revalidate();
            }

            //Scroll to the bottom of the chat
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());

        });
    }


    public void reloadChatMessages(UISettings uiSettings) {
        messagePanel.removeAll();
        ApplicationManager.getApplication().invokeLater(() -> {
            inputField.removeExtension(sendExtension);
            boolean isDark = EditorColorsManager.getInstance().isDarkEditor();
            sendExtension = ExtendableTextComponent.Extension.create(
                    isDark ? LLMCopilotIcons.SendIconDark : LLMCopilotIcons.SendIconLight, "Send message", this::onSendClick);
            inputField.addExtension(sendExtension);
        });
        initChatMessages();
    }

    public void sendChatAlert(String message, String stackTrace) {
        // Send chat alert
        JPanel errorPanel = new JPanel(new BorderLayout());
        AnAction action = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                StackTraceDialogWrapper stackTraceDialogWrapper = new StackTraceDialogWrapper(stackTrace);
                stackTraceDialogWrapper.showAndGet();
            }
        };
        action.getTemplatePresentation().setText("Show Stack-Trace");
        action.getTemplatePresentation().setIcon(AllIcons.General.Error);

        message = "<font color=\"red\">" + message + "</font>";
        String finalMessage = message;
        ApplicationManager.getApplication().invokeLater(() ->{
            errorPanel.add(new ChatResponseField(finalMessage, project, this), BorderLayout.CENTER);
            if(!stackTrace.isEmpty()){

                ActionGroup actionGroup = new ActionGroup() {
                    @Override
                    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
                        return new AnAction[]{action};
                    }
                };

                ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("CodeToolbar", actionGroup, true);
                actionToolbar.setTargetComponent(errorPanel);

                errorPanel.add(actionToolbar.getComponent(), BorderLayout.NORTH);
            }
            messagePanel.add(errorPanel);
            messagePanel.revalidate();
        });

    }

    private void onSendClick(){
        inputField.setEnabled(false);
        String message = inputField.getText();
        inputField.setText("");
        ExtendableTextComponent.Extension loadingExtension = ExtendableTextComponent.Extension.create(new AnimatedIcon.Default(), null, null);
        inputField.addExtension(loadingExtension);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {

            ProgressManager.checkCanceled();
            sendMessage(message);

            ApplicationManager.getApplication().invokeLater(() -> {
                inputField.removeExtension(loadingExtension);
                inputField.setEnabled(true);

                //Scroll to the bottom of the chat
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }
}
