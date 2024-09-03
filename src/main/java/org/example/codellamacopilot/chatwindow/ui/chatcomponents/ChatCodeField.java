package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.google.common.base.Throwables;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        String code = html.text();

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();

        this.setLayout(new BorderLayout());
        JTextPane textPane = new JTextPane();
        textPane.setBackground(scheme.getDefaultBackground());

        AnAction copyAction = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                StringSelection stringSelection = new StringSelection(html.text());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        };
        copyAction.getTemplatePresentation().setIcon(AllIcons.Actions.Copy);
        copyAction.getTemplatePresentation().setText("Copy");

        AnAction createClassAction = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("Create Class");
                createClassFile(code);
            }
        };
        createClassAction.getTemplatePresentation().setIcon(AllIcons.Actions.AddFile);
        createClassAction.getTemplatePresentation().setText("Create New Class");

        AnAction insertAtCaretAction = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                insertCodeAtCaret(code);
            }
        };
        insertAtCaretAction.getTemplatePresentation().setIcon(AllIcons.Duplicates.SendToTheLeftGrayed);
        insertAtCaretAction.getTemplatePresentation().setText("Insert Code At Caret");

        boolean isClass = code.contains("class");

        ActionGroup actionGroup = new ActionGroup() {
            @Override
            public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
                if (isClass) {
                    return new AnAction[]{copyAction, createClassAction, insertAtCaretAction};
                } else {
                    return new AnAction[]{copyAction, insertAtCaretAction};
                }
            }
        };

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("CodeToolbar", actionGroup, true);
        actionToolbar.setTargetComponent(this);

        this.add(actionToolbar.getComponent(), BorderLayout.NORTH);

        textPane.setContentType("text/html");
        textPane.setText(html.html());
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.setEditable(false);
        textPane.setOpaque(true);


        scrollPane.setViewportView(textPane);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void createClassFile(String code) {
        try{
            // Create class file from code
            com.intellij.openapi.editor.Document currentDocument = FileEditorManager.getInstance(PROJECT)
                    .getSelectedTextEditor().getDocument();
            VirtualFile documentFile = FileDocumentManager.getInstance().getFile(currentDocument);

            //Extract the class name from the code
            String className = code.substring(code.indexOf("class") + 5, code.indexOf("{")).trim();
            String filePath = documentFile.getPath()
                    .replace(documentFile.getName(), className + ".java");
            System.out.println("File created at:" + filePath);
            //Create the file
            try {
                createFileUntilSuccess(filePath, code);
            } catch (IOException e) {
                chatWindow.sendChatAlert("Error while creating file", Throwables.getStackTraceAsString(e));
            }

        }catch (NullPointerException e){
            //Send an alert if no editor is selected
            chatWindow.sendChatAlert("Please select a editor to create the class in the same folder.", "");
        }
    }

    private void createFileUntilSuccess(String filePath, String code) throws IOException {
        // Create file until success
        File file = new File(filePath);
        int i = 1;
        while (!file.createNewFile()) {
            filePath = filePath.replace(".java", "(" + i + ")" + ".java");
            file = new File(filePath);
            i++;
        }

        FileUtils.writeStringToFile(file, code, "UTF-8", false);

    }

    private void insertCodeAtCaret(String code) {
        // Insert code at caret
        com.intellij.openapi.editor.Document currentDocument = FileEditorManager.getInstance(PROJECT)
                .getSelectedTextEditor().getDocument();
        int offset = FileEditorManager.getInstance(PROJECT)
                .getSelectedTextEditor().getCaretModel().getOffset();

        WriteCommandAction.runWriteCommandAction(PROJECT, () -> {
            currentDocument.insertString(offset, code);
        });
    }
}
