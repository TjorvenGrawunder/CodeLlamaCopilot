package org.example.codellamacopilot.dialogs;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.apache.http.annotation.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Experimental
public class CodePropositionDialogWrapper extends DialogWrapper {

    private List<String> propositions;
    private int currentProposition = 0;
    private Project project;
    private Editor editor;

    public CodePropositionDialogWrapper(List<String> propositions, Project project, Editor editor) {
        super(true); // use current window as parent
        this.propositions = propositions;
        this.editor = editor;
        this.project = project;
        setTitle("Choose a Code Proposition");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JPanel codePanel = getPanel();
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");
        JButton acceptButton = new JButton("Accept");

        nextButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) codePanel.getLayout();
            if (currentProposition == propositions.size() - 1) {
                currentProposition = 0;
            } else {
                currentProposition++;
            }
            cardLayout.next(codePanel);
        });

        previousButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) codePanel.getLayout();
            if (currentProposition == 0) {
                currentProposition = propositions.size() - 1;
            } else {
                currentProposition--;
            }
            cardLayout.previous(codePanel);
        });

        acceptButton.addActionListener(e -> {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                JScrollPane scrollPane = (JScrollPane) codePanel.getComponent(currentProposition);
                JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
                editor.getDocument().insertString(editor.getCaretModel().getOffset(), textArea.getText());
            });
            this.close(0);
        });

        dialogPanel.add(codePanel, BorderLayout.CENTER);
        buttonPanel.add(nextButton, BorderLayout.EAST);
        buttonPanel.add(previousButton, BorderLayout.WEST);
        buttonPanel.add(acceptButton, BorderLayout.CENTER);

        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        return dialogPanel;
    }

    private @NotNull JPanel getPanel() {
        JPanel codePanel = new JPanel(new CardLayout());
        for (String proposition : propositions) {

            JBScrollPane scrollPane = new JBScrollPane();
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            JTextArea textArea = new JTextArea(proposition);
            textArea.setEditable(false);
            textArea.setLineWrap(false);
            textArea.setSize(400, 400);

            scrollPane.setViewportView(textArea);
            codePanel.add(scrollPane, proposition);
        }
        return codePanel;
    }
}
