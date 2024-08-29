package org.example.codellamacopilot.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class StackTraceDialogWrapper extends DialogWrapper {
    private String stackTrace;

    public StackTraceDialogWrapper(String stackTrace) {
        super(true);
        this.stackTrace = stackTrace;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel stackTracePanel = new JPanel(new BorderLayout());
        JBScrollPane stackTraceScrollPane = new JBScrollPane();

        stackTraceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stackTraceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JTextPane stackTraceTextPane = new JTextPane();
        stackTraceTextPane.setContentType("text/html");
        stackTrace = "<font color=\"red\">" + stackTrace + "</font>";
        stackTraceTextPane.setText(stackTrace);
        stackTraceTextPane.setEditable(false);
        stackTraceScrollPane.setViewportView(stackTraceTextPane);

        stackTracePanel.add(stackTraceScrollPane, BorderLayout.CENTER);

        return stackTracePanel;
    }
}
