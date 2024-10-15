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
import org.example.codellamacopilot.dialogs.ChangeChatPromptDialogWrapper;
import org.example.codellamacopilot.dialogs.StackTraceDialogWrapper;
import org.example.codellamacopilot.exceptions.ErrorMessageException;
import org.example.codellamacopilot.exceptions.MissingModelException;
import org.example.codellamacopilot.icons.LLMCopilotIcons;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ChatWindow is the main chat window that contains the chat messages and the input field. It will be
 * displayed as a tool window in the IDE.
 */
public class ChatWindow {
    private JPanel mainPanel;
    private JPanel messagePanel;
    private JBScrollPane scrollPane;
    private ExpandableTextField inputField;
    private ChatClient chatClient;
    private Project project;
    private ExtendableTextComponent.Extension sendExtension;

    private final String helpMessage = "<h2 id=\"help-for-commands\">Help for Commands</h2>\n" +
            "<ul>\n" +
            "<li>\\test: generates JUnit-TestFile for the currently opened editor</li>\n" +
            "<li>\\explain: explains the code in the currently opened editor</li>\n" +
            "<li>\\debug: searches for bugs in the currently opened editor</li>\n" +
            "<li>\\clear: cleares the chat history</li>\n" +
            "</ul>";

    /**
     * Creates the chat window and initializes the chat messages and main components
     * @param project the project
     */
    public ChatWindow(Project project) {
        this.project = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Reload chat messages when the UI settings change
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        bus.connect().subscribe(UISettingsListener.TOPIC, (UISettingsListener) this::reloadChatMessages);

        this.chatClient = new ChatClient(this.project, true);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        scrollPane = new JBScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel inputPanel = new JPanel(new BorderLayout());

        inputField = new ExpandableTextField();

        // Check if theme is dark and set the send icon accordingly
        boolean isDark = EditorColorsManager.getInstance().isDarkEditor();
        sendExtension = ExtendableTextComponent.Extension.create(
                isDark ? LLMCopilotIcons.SendIconDark: LLMCopilotIcons.SendIconLight, "Send message", this::onSendClick);

        inputField.addExtension(sendExtension);

        inputField.addActionListener(e -> {
            onSendClick();
        });

        AnAction changePromptAction = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ChangeChatPromptDialogWrapper changeChatPromptDialogWrapper = new ChangeChatPromptDialogWrapper();
                changeChatPromptDialogWrapper.showAndGet();
            }
        };
        changePromptAction.getTemplatePresentation().setIcon(AllIcons.Expui.FileTypes.Text);
        changePromptAction.getTemplatePresentation().setText("Change Chat Prompt");

        ActionGroup actionGroup = new ActionGroup() {
            @Override
            public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
                return new AnAction[]{changePromptAction};
            }
        };

        // Add the ActionGroup to an ActionToolbar
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("CodeToolbar", actionGroup, true);
        actionToolbar.setTargetComponent(inputPanel);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(actionToolbar.getComponent(), BorderLayout.NORTH);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        initChatMessages();
    }

    public JComponent getContent() {
        return mainPanel;
    }

    /**
     * Sends a message to the chat client and displays the response in the chat window
     * @param message the message to send
     */
    private void sendMessage(String message) {
        ChatElement chatElement = new ChatElement(message);
        messagePanel.add(chatElement);

        //Scroll to the bottom of the chat
        ApplicationManager.getApplication().invokeLater(() -> {
            messagePanel.validate();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        boolean skipResponse = false;
        String response;
        try {
            // Check if the message is a command
            switch (message) {
                case "\\debug" -> response = chatClient.debug();
                case "\\explain" -> response = chatClient.explain();
                case "\\help" -> response = helpMessage;
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
                // Default case is to send the message to the chat client
                default -> response = chatClient.sendMessage(message);
            }

            if(!skipResponse){
                // Split the response into parts based on the code blocks
                //String[] messageParts = response.split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|js|lua|make|markdown|php|python|r|sql|tex|text|xml|groovy))|```");

                ChatResponseField chatResponseField = getChatResponseField(response);
                ApplicationManager.getApplication().invokeLater(() -> {
                    messagePanel.add(chatResponseField);
                    messagePanel.revalidate();
                });
            }

            //Scroll to the bottom of the chat
            ApplicationManager.getApplication().invokeLater(() -> {
                messagePanel.validate();
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }catch (ErrorMessageException errorMessageException){
            // Handle error message from the server
            chatClient.getRequestFormat().removeLastMessage();
            sendChatAlert(errorMessageException.getMessage(), "");
        } catch (MissingModelException e) {
            // Handle missing model exception
            sendChatAlert("Please select a chat model in the settings", "");
        }
        catch (Exception e) {
            // Handle other exceptions
            chatClient.getRequestFormat().removeLastMessage();
            String stackTrace = Throwables.getStackTraceAsString(e);

            sendChatAlert("An error occurred while sending the message. Please try again.", stackTrace);
        }
    }

    /**
     * Initialize the chat messages at the start of the IDE
     */
    private void initChatMessages() {
        ChatHistoryManipulator chatHistoryManipulator = new ChatHistoryManipulator();
        if (chatHistoryManipulator.getMessages().size() == 1 + chatHistoryManipulator.getContextCounter()) {
            // First message
            ChatElement chatElement = new ChatElement("Hello! I'm your personal programming assistant. How can I help you today?");
            messagePanel.add(chatElement);
        }

        // Go through the chat history and display the messages
        ApplicationManager.getApplication().invokeLater(() -> {
            for (MessageObject message : chatHistoryManipulator.getMessages()) {
                if (message.getRole().equals("user")) {
                    ChatElement chatElement = new ChatElement(message.getContent());
                    messagePanel.add(chatElement);
                } else if (message.getRole().equals("assistant")) {
                    ChatResponseField chatResponseField = getChatResponseField(message.getContent());
                    messagePanel.add(chatResponseField);
                }
                messagePanel.revalidate();
            }

            //Scroll to the bottom of the chat
            messagePanel.validate();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());

        });
    }

    /**
     * Get the chat response field based on the message
     * @param message the message
     * @return the chat response field
     */
    private @NotNull ChatResponseField getChatResponseField(String message) {
        //String[] messageParts = message.split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|json|js|lua|make|markdown|php|python|r|sql|tex|yaml|text|xml|groovy|kotlin))|```");
        String[] messageParts = splitAtCodeBlocks(message).toArray(new String[0]);
        ChatResponseField chatResponseField;
        try {
            chatResponseField = new ChatResponseField(messageParts, project, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chatResponseField;
    }


    /**
     * Reload the chat messages when the UI settings change
     * @param uiSettings the UI settings
     */
    private void reloadChatMessages(UISettings uiSettings) {
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

    /**
     * Send a chat alert with a message and stack trace. Can be used to display errors to the user.
     * @param message the message
     * @param stackTrace the stack trace
     */
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
            try {
                errorPanel.add(new ChatResponseField(finalMessage, project, this), BorderLayout.CENTER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    /**
     * Send the message when the send button is clicked. Disables the input field while the message is being sent.
     * The message is sent on a separate thread to prevent the UI from freezing.
     * Display Loading icon while the message is being sent.
     */
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

    /**
     * Split the markdown text into parts based on code blocks
     * @param markdown
     * @return the list of text and code blocks
     */
    public static List<String> splitAtCodeBlocks(String markdown) {
        // Regular expression to match either text or code blocks
        // The code block is captured in the parentheses (```.*?```)
        Pattern pattern = Pattern.compile("(?s)(```.*?```)");
        Matcher matcher = pattern.matcher(markdown);

        List<String> result = new ArrayList<>();
        int lastEnd = 0;

        while (matcher.find()) {
            // Add the text before the code block
            if (lastEnd < matcher.start()) {
                String textBeforeCodeBlock = markdown.substring(lastEnd, matcher.start()).trim();
                if (!textBeforeCodeBlock.isEmpty()) {
                    result.add(textBeforeCodeBlock);
                }
            }

            // Add the code block itself
            String codeBlock = matcher.group();
            result.add(codeBlock);

            // Update the last end position
            lastEnd = matcher.end();
        }

        // Add any remaining text after the last code block
        if (lastEnd < markdown.length()) {
            String remainingText = markdown.substring(lastEnd).trim();
            if (!remainingText.isEmpty()) {
                result.add(remainingText);
            }
        }

        return result;
    }
}
