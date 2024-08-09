package org.example.codellamacopilot.chatwindow.ui.chatcomponents;


import com.intellij.lang.Language;
import com.intellij.markdown.utils.lang.CodeBlockHtmlSyntaxHighlighter;
import com.intellij.markdown.utils.lang.HtmlSyntaxHighlighter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;

import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextComponent;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import org.example.codellamacopilot.chatwindow.api.ChatClient;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

import java.awt.*;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class ChatWindow {
    private JPanel mainPanel;
    private JPanel messagePanel;
    private JScrollPane scrollPane;
    private ExpandableTextField inputField;
    private ChatClient chatClient;
    private Project project;

    public ChatWindow(Project project) {
        this.project = project;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        this.chatClient = new ChatClient(this.project, CopilotSettingsState.getInstance().usedChatModel);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        scrollPane = new JBScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new ExpandableTextField();

        inputField.addActionListener(e -> {

            inputField.setEnabled(false);
            String message = inputField.getText();
            inputField.setText("");
            inputField.addExtension(ExtendableTextComponent.Extension.create(new AnimatedIcon.Default(), null, null));

            ApplicationManager.getApplication().executeOnPooledThread(() -> {

                ProgressManager.checkCanceled();
                sendMessage(message);

                ApplicationManager.getApplication().invokeLater(() -> {
                    inputField.removeExtension(inputField.getExtensions().get(inputField.getExtensions().size() - 1));
                    inputField.setEnabled(true);
                });
            });
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
    }

    public JComponent getContent() {
        return mainPanel;
    }

    private void sendMessage(String message) {
        ChatElement chatElement = new ChatElement(message);
        messagePanel.add(chatElement);
        messagePanel.revalidate();

        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED).build());
        Browser browser = engine.newBrowser();
        BrowserView view = BrowserView.newInstance(browser);
        messagePanel.add(view, BorderLayout.CENTER);

        boolean skipResponse = false;
        String response;
        if(message.equals("\\debug")){
            response = chatClient.debug();
        } else if(message.equals("\\explain")){
            response = chatClient.explain();
        } else if(message.equals("\\test")) {
            response = chatClient.test();
            //skipResponse = true;
        } else {
            response = chatClient.sendMessage(message);
        }

        if(!skipResponse){
            Parser parser = Parser.builder().build();
            Document document = parser.parse(response);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            org.jsoup.nodes.Document doc = Jsoup.parse(renderer.render(document));

            //Highlight code blocks
            highlightCodeBlocks(doc, project);

            JTextPane textPane = new JTextPane();
            textPane.setContentType("text/html");
            String html = doc.html();
            textPane.setText(html);
            textPane.setFont(new Font("Arial", Font.PLAIN, 14));
            textPane.setEditable(false);
            textPane.setOpaque(true);

            textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            messagePanel.add(textPane);
            messagePanel.revalidate();

        }

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    private void highlightCodeBlocks(org.jsoup.nodes.Document doc, Project project) {
        //Get all code blocks
        Elements codeBlocks = doc.select("code");
        //Remove code blocks that are too short
        codeBlocks.removeIf(element -> element.text().length() < 50);

        for (Element codeBlock : codeBlocks) {
            String code = codeBlock.text();
            //Highlight code block
            HtmlChunk highlightedCode = HtmlSyntaxHighlighter.Companion.colorHtmlChunk(project, Language.findLanguageByID("JAVA"), code);
            codeBlock.html(highlightedCode.toString());
        }
    }
}
