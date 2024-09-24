package org.example.codellamacopilot.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.example.codellamacopilot.chatwindow.persistentchathistory.ChatHistoryManipulator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChangeChatPromptDialogWrapper extends DialogWrapper {
    private ChatHistoryManipulator chatHistoryManipulator;
    private JBTextArea changeChatPromptTextArea;

    public ChangeChatPromptDialogWrapper() {
        super(true);
        chatHistoryManipulator = new ChatHistoryManipulator();
        this.setSize(800, 600);
        this.setTitle("Change Chat Prompt");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel changeChatPromptPanel = new JPanel(new BorderLayout());
        changeChatPromptTextArea = new JBTextArea();
        changeChatPromptTextArea.setText(chatHistoryManipulator.getFirstSystemPrompt());
        changeChatPromptTextArea.setEditable(true);
        changeChatPromptTextArea.setLineWrap(true);
        changeChatPromptTextArea.setWrapStyleWord(true);
        changeChatPromptTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JBScrollPane scrollPane = new JBScrollPane(changeChatPromptTextArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        changeChatPromptPanel.add(scrollPane, BorderLayout.CENTER);

        return changeChatPromptPanel;
    }

    @Override
    protected void doOKAction() {
        ChatHistoryManipulator chatHistoryManipulator = new ChatHistoryManipulator();
        chatHistoryManipulator.setSystemPrompt(changeChatPromptTextArea.getText());
        super.doOKAction();
    }
}
