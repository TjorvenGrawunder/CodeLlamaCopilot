package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import javax.swing.*;

public class ChatTextElement extends JTextPane {
    public ChatTextElement(String message) {
        super();
        this.setContentType("text/html");
        this.setText(message);
        this.setEditable(false);
        this.setOpaque(false);
    }
}
