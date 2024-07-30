package org.example.codellamacopilot.chatwindow.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.ui.UIUtil;
import org.example.codellamacopilot.chatwindow.api.ChatClient;
import org.example.codellamacopilot.chatwindow.parser.ChatGPTParser;
import org.example.codellamacopilot.chatwindow.requestformats.ChatGPTRequestFormat;
import org.example.codellamacopilot.chatwindow.ui.chatcomponents.ChatWindow;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ChatToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //ChatToolWindowContent chatToolWindowContent = new ChatToolWindowContent(toolWindow, project);
        //Content content = ContentFactory.getInstance().createContent(chatToolWindowContent.getChatPanel(), "", false);
        ChatWindow chatWindow = new ChatWindow(project);
        toolWindow.getComponent().add(chatWindow.getContent());
    }

    private static class ChatToolWindowContent{

        private ChatClient chatClient;


        private final JPanel BASE_PANEL = new JPanel();
        private final JPanel INPUT_PANEL = new JPanel();
        //private final JTextArea CHAT_MESSAGE_AREA = new JBTextArea();
        private final RSyntaxTextArea CHAT_MESSAGE_AREA = new RSyntaxTextArea();
        private final JScrollPane MESSAGE_SCROLL_PANE = new JBScrollPane(CHAT_MESSAGE_AREA, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        private final JTextField CHAT_INPUT_FIELD = new JTextField();
        private final JButton CHAT_SEND_BUTTON = new JButton("Send");
        private final JLabel CHAT_TITLE = new JBLabel("CodeLlama copilot chat");

        public ChatToolWindowContent(ToolWindow toolWindow, Project project) {
            this.chatClient = new ChatClient(project, new ChatGPTRequestFormat());
            //CHAT_MESSAGE_AREA.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JAVA);
            //CHAT_MESSAGE_AREA.setCodeFoldingEnabled(true);
            BASE_PANEL.setLayout(new BorderLayout(0, 0));
            INPUT_PANEL.setLayout(new BoxLayout(INPUT_PANEL, BoxLayout.X_AXIS));
            //MESSAGE_SCROLL_PANE.setLayout(new ScrollPaneLayout());
            BASE_PANEL.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            INPUT_PANEL.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            INPUT_PANEL.add(CHAT_INPUT_FIELD);
            INPUT_PANEL.add(CHAT_SEND_BUTTON);
            //CHAT_MESSAGE_AREA.setEditable(false);
            BASE_PANEL.add(INPUT_PANEL, BorderLayout.SOUTH);
            //BASE_PANEL.add(MESSAGE_SCROLL_PANE, BorderLayout.CENTER);
            BASE_PANEL.add(CHAT_TITLE, BorderLayout.NORTH);
            //BASE_PANEL.add(CHAT_PANEL, BorderLayout.CENTER);
            if(UIUtil.isUnderDarcula()){
            //    CHAT_MESSAGE_AREA.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            //    CHAT_MESSAGE_AREA.setBackground(JBColor.background());
            //    CHAT_MESSAGE_AREA.setForeground(JBColor.foreground());
            //    CHAT_MESSAGE_AREA.setHighlightCurrentLine(true);
            //    CHAT_MESSAGE_AREA.setCurrentLineHighlightColor(new Color(30, 30, 30));
            //    CHAT_MESSAGE_AREA.setMarginLineEnabled(true);
            //    CHAT_MESSAGE_AREA.setMarginLineColor(new Color(60, 60, 60));
            }
            CHAT_SEND_BUTTON.addActionListener(e -> {
                sendMessage(CHAT_INPUT_FIELD.getText());
                CHAT_INPUT_FIELD.setText("");
            });

            CHAT_INPUT_FIELD.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        sendMessage(CHAT_INPUT_FIELD.getText());
                        CHAT_INPUT_FIELD.setText("");
                    }
                }
            });
        }

        public JComponent getChatPanel() {
            return BASE_PANEL;
        }

        private void sendMessage(String message){
            String response = "";
            if(message.equals("\\explain")){
                response = chatClient.explain();
                CHAT_MESSAGE_AREA.append("You: \nPlease explain my code! \n");
                CHAT_MESSAGE_AREA.append("CodeLlama Copilot: \n" + response + "\n");
            }else if(message.equals("\\debug")){
                response = chatClient.debug();
                CHAT_MESSAGE_AREA.append("You: \nPlease debug my code! \n");
                CHAT_MESSAGE_AREA.append("CodeLlama Copilot: \n" + response + "\n");
            }else{
                response = chatClient.sendMessage(message);
                CHAT_MESSAGE_AREA.append("You: \n" + message + "\n");
                CHAT_MESSAGE_AREA.append("CodeLlama Copilot: \n" + response + "\n");
            }
        }
    }
}
