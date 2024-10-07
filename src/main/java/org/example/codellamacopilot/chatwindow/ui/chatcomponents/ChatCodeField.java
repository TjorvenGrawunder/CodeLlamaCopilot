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
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
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
import java.util.StringTokenizer;

/**
 * Chat element to show code responses from the chatbot with extra Buttons
 */
public class ChatCodeField extends JPanel {

    private final Project PROJECT;
    private ChatWindow chatWindow;

    /**
     * Creates a chat code field
     * @param html the html document containing the code
     * @param project the project
     * @param chatWindow the chat window
     */
    public ChatCodeField(Document html, Project project, ChatWindow chatWindow) {
        super();
        this.PROJECT = project;
        this.chatWindow = chatWindow;
        String code = html.text();

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(5, 0));

        this.setLayout(new BorderLayout(3, 0));
        JTextPane textPane = new JTextPane();

        // Set the background color of the text pane to the default background color of the IDE
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        textPane.setBackground(scheme.getDefaultBackground());

        // Create all actions
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

        // Create ActionGroup from created actions
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

        // Add the ActionGroup to an ActionToolbar
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("CodeToolbar", actionGroup, true);
        actionToolbar.setTargetComponent(this);

        this.add(actionToolbar.getComponent(), BorderLayout.NORTH);

        textPane.setContentType("text/html");
        textPane.setText(html.html());
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.setEditable(false);
        textPane.setOpaque(true);


        scrollPane.setViewportView(textPane);
        this.add(spacePanel, BorderLayout.WEST);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Method that is executed when the createClass action is triggered. This method creates a new class file in the same
     * folder as the current file.
     * @param code the code to be written to the new class file
     */
    private void createClassFile(String code) {
        try{
            // Create class file from code
            com.intellij.openapi.editor.Document currentDocument = FileEditorManager.getInstance(PROJECT)
                    .getSelectedTextEditor().getDocument();
            VirtualFile documentFile = FileDocumentManager.getInstance().getFile(currentDocument);

            //Extract the class name from the code
            StringTokenizer tokenizer = new StringTokenizer(code.substring(code.indexOf("class") + 5)," <" );

            String className =  tokenizer.nextToken();
            String filePath = documentFile.getPath()
                    .replace(documentFile.getName(), className + ".java");
            //Create the file until unique name is found
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

    /**
     * Method that creates a file with the given code at the given file path. If the file already exists, the method
     * will append a number to the file name until a unique file name is found. This method will be called by the
     * createClassFile method.
     * @param filePath the path of the file to be created
     * @param code the code to be written to the file
     * @throws IOException if an error occurs while creating the file
     */
    private void createFileUntilSuccess(String filePath, String code) throws IOException {
        // Create file until success
        File file = new File(filePath);
        int i = 1;
        while (!file.createNewFile()) {
            // Append number to file name until unique name is found
            filePath = filePath.replace(".java", "(" + i + ")" + ".java");
            file = new File(filePath);
            i++;
        }

        FileUtils.writeStringToFile(file, code, "UTF-8", false);

    }

    /**
     * Method that is executed when the insertAtCaret action is triggered. This method inserts the code at the current
     * caret position in the editor.
     * @param code the code to be inserted
     */
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
