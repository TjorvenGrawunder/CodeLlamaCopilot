package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.intellij.lang.Language;
import com.intellij.markdown.utils.lang.CodeBlockHtmlSyntaxHighlighter;
import com.intellij.markdown.utils.lang.HtmlSyntaxHighlighter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;

import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextComponent;
import com.intellij.util.ui.ExtendableHTMLViewFactory;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.JBHtmlEditorKit;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import org.example.codellamacopilot.chatwindow.api.ChatClient;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistory;
import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistoryManipulator;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.responseobjects.chatgpt.MessageObject;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.text.View;

import java.awt.*;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class ChatWindow {
    private JPanel mainPanel;
    private JPanel messagePanel;
    private JBScrollPane scrollPane;
    private ExpandableTextField inputField;
    private ChatClient chatClient;
    private Project project;

    public ChatWindow(Project project) {
        this.project = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        this.chatClient = new ChatClient(this.project, CopilotSettingsState.getInstance().usedChatModel, true);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        scrollPane = new JBScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new ExpandableTextField();

        inputField.addActionListener(e -> {

            inputField.setEnabled(false);
            String message = inputField.getText();
            inputField.setText("");
            inputField.addExtension(ExtendableTextComponent.Extension.create(new AnimatedIcon.Default(), null, null));

            ApplicationManager.getApplication().executeOnPooledThread(() -> {

                ProgressManager.checkCanceled();
                sendMessage(message);

                ApplicationManager.getApplication().invokeLater(() -> {
                    inputField.removeExtension(inputField.getExtensions().get(inputField.getExtensions().size() - 1));
                    inputField.setEnabled(true);

                    //Scroll to the bottom of the chat
                    JScrollBar vertical = scrollPane.getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());
                });
            });
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
        if(message.equals("\\debug")){
            response = chatClient.debug();
        } else if(message.equals("\\explain")){
            response = chatClient.explain();
        } else if(message.equals("\\test")) {
            response = chatClient.test();
            //skipResponse = true;
        } else {
            response = chatClient.sendMessage(message);
        }

        if(!skipResponse){
            String[] messageParts = response.split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|js|lua|make|markdown|php|python|r|sql|tex|text|xml|groovy))|```");
            /*for(String codePart: codeParts){
                if (codePart.startsWith("```java")) {
                    codePart += "```";
                }
            }*/

            messagePanel.add(new ChatResponseField(messageParts, project, this));
            messagePanel.revalidate();

        }

    }

    private void initChatMessages() {
        ChatHistoryManipulator chatHistoryManipulator = new ChatHistoryManipulator();
        if(chatHistoryManipulator.getMessages().size() == 1 + chatHistoryManipulator.getContextCounter()) {
            // Initialize chat messages
            ChatElement chatElement = new ChatElement("Hello! I'm your personal programming assistant. How can I help you today?");
            messagePanel.add(chatElement);
        }
        for (MessageObject message : chatHistoryManipulator.getMessages()) {
            if(message.getRole().equals("user")) {
                ChatElement chatElement = new ChatElement(message.getContent());
                messagePanel.add(chatElement);
            }else if (message.getRole().equals("assistant")) {
                String[] messageParts = message.getContent().split("(?=```(java|html|bash|bat|c|cmake|cpp|csharp|css|gitignore|ini|js|lua|make|markdown|php|python|r|sql|tex|text|xml|groovy))|```");
                ChatResponseField chatResponseField = new ChatResponseField(messageParts, project, this);
                messagePanel.add(chatResponseField);
            }
            messagePanel.revalidate();

            //Scroll to the bottom of the chat
            ApplicationManager.getApplication().invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }

    }

    public void sendChatAlert(String message) {
        // Send chat alert
        message = "<font color=\"red\">" + message + "</font>";
        sendMessage(message);
    }
}
