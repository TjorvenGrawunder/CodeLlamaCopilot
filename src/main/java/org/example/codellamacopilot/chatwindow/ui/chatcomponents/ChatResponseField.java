package org.example.codellamacopilot.chatwindow.ui.chatcomponents;

import com.intellij.lang.Language;
import com.intellij.markdown.utils.lang.HtmlSyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.HtmlChunk;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

public class ChatResponseField extends JPanel {

    private ChatWindow chatWindow;

    public ChatResponseField(String response, Project project, ChatWindow chatWindow){
        this(new String[]{response}, project, chatWindow);
    }

    public ChatResponseField(String[] response, Project project, ChatWindow chatWindow) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (String s : response) {

            Parser parser = Parser.builder().build();
            Document document = parser.parse(s);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            org.jsoup.nodes.Document doc = Jsoup.parse(renderer.render(document));

            if (s.startsWith("```java")) {
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
            //Highlight code block
            HtmlChunk highlightedCode = HtmlSyntaxHighlighter.Companion.colorHtmlChunk(project, Language.findLanguageByID("JAVA"), code);
            codeBlock.html(highlightedCode.toString());
        }
    }
}
