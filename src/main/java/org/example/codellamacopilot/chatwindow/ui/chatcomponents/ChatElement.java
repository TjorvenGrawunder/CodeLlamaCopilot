package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ChatElement extends JPanel {
    private JTextArea messageTextArea;

    public ChatElement(String message) {
        this.setLayout(new BorderLayout());
        messageTextArea = new JTextArea(message);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setLineWrap(true);
        messageTextArea.setEditable(false);
        messageTextArea.setOpaque(false); // Set transparent background
        messageTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Dimension preferredSize = messageTextArea.getPreferredSize();
        messageTextArea.setSize(preferredSize.width, preferredSize.height);
        messageTextArea.setMinimumSize(preferredSize);

        this.add(messageTextArea, BorderLayout.CENTER);

        // Optional: Set a background color for the chat element
        this.setBackground(JBColor.LIGHT_GRAY);
        this.setBorder(BorderFactory.createLineBorder(JBColor.GRAY, 1));
    }
}
