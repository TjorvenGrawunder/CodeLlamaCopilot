package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import com.intellij.lang.Language;
import com.intellij.markdown.utils.lang.HtmlSyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.HtmlChunk;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.webjars.WebJarAssetLocator;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChatResponseField extends JPanel {

    private ChatWindow chatWindow;

    public ChatResponseField(String response, Project project, ChatWindow chatWindow) throws IOException {
        this(new String[]{response}, project, chatWindow);
    }

    public ChatResponseField(String[] response, Project project, ChatWindow chatWindow) throws IOException {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (String s : response) {

            Parser parser = Parser.builder().build();
            Node document = parser.parse(s);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            org.jsoup.nodes.Document doc = Jsoup.parse(renderer.render(document));

            if (s.startsWith("```")) {
                highlightCodeBlocks(doc, project);
                this.add(new ChatCodeField(doc, project, chatWindow));
            } else{
                this.add(new ChatTextElement(doc.html()));
            }
        }
    }

    private void highlightCodeBlocks(org.jsoup.nodes.Document doc, Project project) {
        //Get all code blocks
        Elements codeBlocks = doc.select("code");

        //Remove code blocks that are too short
        codeBlocks.removeIf(element -> element.text().length() < 50);

        for (Element codeBlock : codeBlocks) {
            String code = codeBlock.text();

            String className = codeBlock.className();
            String languageId = null;

            // Extract language identifier from class attribute (e.g., "language-java")
            if (className.startsWith("language-")) {
                languageId = className.substring(9);
                languageId = convertLanguageID(languageId);
            }

            //Highlight code block
            if (languageId != null) {
                HtmlChunk highlightedCode = HtmlSyntaxHighlighter.Companion.colorHtmlChunk(project, Language.findLanguageByID(languageId), code);
                codeBlock.html(highlightedCode.toString());
            }
        }
    }

    private String convertLanguageID(String languageName){
        return switch (languageName) {
            case "java" -> "JAVA";
            case "kotlin" -> "kotlin";
            case "html" -> "HTML";
            case "groovy" -> "Groovy";
            case "xml" -> "XML";
            case "json" -> "JSON";
            case "yaml" -> "yaml";
            case "markdown" -> "Markdown";
            default -> null;
        };
    }
}
