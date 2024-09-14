package org.example.codellamacopilot.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog to display a stack trace
 */
public class StackTraceDialogWrapper extends DialogWrapper {
    private String stackTrace;

    public StackTraceDialogWrapper(String stackTrace) {
        super(true);
        this.stackTrace = stackTrace;
        this.setSize(800, 600);
        this.setTitle("Stack Trace");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel stackTracePanel = new JPanel(new BorderLayout());
        JBTextArea stackTraceTextArea = new JBTextArea();
        stackTraceTextArea.setText(stackTrace);
        stackTraceTextArea.setEditable(false);
        stackTraceTextArea.setLineWrap(false);
        stackTraceTextArea.setWrapStyleWord(true);
        stackTraceTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JBScrollPane scrollPane = new JBScrollPane(stackTraceTextArea);
        stackTracePanel.add(scrollPane, BorderLayout.CENTER);

        return stackTracePanel;
    }
}
