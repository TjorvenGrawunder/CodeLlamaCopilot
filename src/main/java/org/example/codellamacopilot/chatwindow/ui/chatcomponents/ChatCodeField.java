package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;

public class ChatCodeField extends JPanel {

    private final Project PROJECT;
    private ChatWindow chatWindow;

    public ChatCodeField(Document html, Project project, ChatWindow chatWindow) {
        super();
        this.PROJECT = project;
        this.chatWindow = chatWindow;
        this.setLayout(new BorderLayout());
        JTextPane textPane = new JTextPane();

        JButton copyButton = new JButton("Copy");
        JButton createClassButton = new JButton("Create Class");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(copyButton);
        String code = html.text();
        if(code.contains("class")){
            buttonPanel.add(createClassButton);
        }

        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(code);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        createClassButton.addActionListener(e -> {
            // Create class from code
            createClassFile(code);
        });

        this.add(buttonPanel, BorderLayout.NORTH);

        textPane.setContentType("text/html");
        textPane.setText(html.html());
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.setEditable(false);
        textPane.setOpaque(true);


        this.add(textPane, BorderLayout.CENTER);
    }

    private void createClassFile(String code) {
        {
            try{
                // Create class file from code
                com.intellij.openapi.editor.Document currentDocument = FileEditorManager.getInstance(PROJECT)
                        .getSelectedTextEditor().getDocument();
                VirtualFile documentFile = FileDocumentManager.getInstance().getFile(currentDocument);
                if (documentFile != null) {
                    //Extract the class name from the code
                    String className = code.substring(code.indexOf("class") + 5, code.indexOf("{")).trim();
                    String filePath = documentFile.getPath()
                            .replace(documentFile.getName(), className + ".java");
                    //Create the file
                    try {
                        File file = new File(filePath);
                        if (file.createNewFile()) {
                            FileUtils.writeStringToFile(file, code, "UTF-8", false);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (NullPointerException e){
                //Send an alert if no editor is selected
                chatWindow.sendChatAlert("Please select a editor to create the class in the same folder.", "");
            }
        }
    }
}
