package org.example.codellamacopilot.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import org.apache.http.annotation.Experimental;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Experimental
public class CodePropositionDialogWrapper extends DialogWrapper {

    List<String> propositions;

    public CodePropositionDialogWrapper(List<String> propositions) {
        super(true); // use current window as parent
        setTitle("Choose a Code Proposition");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JPanel codePanel = new JPanel(new CardLayout());
        for (String proposition : propositions) {
            codePanel.add(new JLabel(proposition), proposition);
        }

        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");
        JButton acceptButton = new JButton("Accept");

        nextButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) codePanel.getLayout();
            cardLayout.next(codePanel);
        });

        previousButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) codePanel.getLayout();
            cardLayout.previous(codePanel);
        });

        dialogPanel.add(codePanel, BorderLayout.CENTER);
        dialogPanel.add(nextButton, BorderLayout.EAST);
        dialogPanel.add(previousButton, BorderLayout.WEST);
        dialogPanel.add(acceptButton, BorderLayout.SOUTH);

        return dialogPanel;
    }
}
