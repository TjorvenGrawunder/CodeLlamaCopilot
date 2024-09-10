package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import javax.swing.*;

/**
 * Simple Text Pane to display messages from the user that are not code
 */
public class ChatTextElement extends JTextPane {
    public ChatTextElement(String message) {
        super();
        this.setContentType("text/html");
        this.setText(message);
        this.setEditable(false);
        this.setOpaque(false);
    }
}
