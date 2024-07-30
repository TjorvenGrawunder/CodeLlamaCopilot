package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ChatCodeField extends RSyntaxTextArea {
    public ChatCodeField(String code){
        super();
        this.setText(code);
        this.setEditable(false);
        this.setSyntaxEditingStyle("text/java");
    }
}
