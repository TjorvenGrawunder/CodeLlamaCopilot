package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;

import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextComponent;
import org.example.codellamacopilot.chatwindow.api.ChatClient;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.example.codellamacopilot.settings.CopilotSettings;

import javax.swing.*;

import java.awt.*;

public class ChatWindow {
    private JPanel mainPanel;
    private JPanel messagePanel;
    private JScrollPane scrollPane;
    private ExpandableTextField inputField;
    private ChatClient chatClient;

    public ChatWindow(Project project) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        this.chatClient = new ChatClient(project, CopilotSettings.getInstance().usedChatModel);

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
                });
            });
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
    }

    public JComponent getContent() {
        return mainPanel;
    }

    private void sendMessage(String message) {
        ChatElement chatElement = new ChatElement(message);
        messagePanel.add(chatElement);
        messagePanel.revalidate();

        //String response = "[$PROFILE$]: extended\n" + chatClient.sendMessage(message);
        //String html = Processor.process(response);

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
            Parser parser = Parser.builder().build();
            Document document = parser.parse(response);
            HtmlRenderer renderer = HtmlRenderer.builder().build();


            JTextPane textPane = new JTextPane();
            textPane.setContentType("text/html");
            textPane.setText(renderer.render(document));
            textPane.setFont(new Font("Arial", Font.PLAIN, 14));
            textPane.setEditable(false);
            textPane.setOpaque(true);

            textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            messagePanel.add(textPane);
            messagePanel.revalidate();

        }

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }
}
