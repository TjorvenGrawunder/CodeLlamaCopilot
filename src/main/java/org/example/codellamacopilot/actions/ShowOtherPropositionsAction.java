package org.example.codellamacopilot.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.apache.http.annotation.Experimental;
import org.example.codellamacopilot.chatwindow.api.ChatClient;
import org.example.codellamacopilot.completionutil.CompletionPropositionsStorage;
import org.example.codellamacopilot.dialogs.CodePropositionDialogWrapper;
import org.example.codellamacopilot.llamaconnection.CompletionClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShowOtherPropositionsAction extends AnAction {

    private final int MAX_PROPOSITIONS = 2;
    /**
     * Shows a dialog to choose between other completion propositions
     * @param e AnActionEvent
     */
    @Experimental
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if(CompletionPropositionsStorage.isValid()){
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            List<String> propositions = new ArrayList<>();
            if(CompletionPropositionsStorage.getCurrentMessage() != null) {
                if (CompletionPropositionsStorage.isInstruct()) {
                    ChatClient chatClient = new ChatClient(e.getProject(), false);
                    try {
                        for (int i = 0; i < MAX_PROPOSITIONS; i++) {
                            propositions.add(chatClient.sendMessage(CompletionPropositionsStorage.getCurrentMessage(), true, CompletionPropositionsStorage.getCurrentLine()));
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    CompletionClient client = new CompletionClient();
                    for (int i = 0; i < MAX_PROPOSITIONS; i++) {
                        try {
                            propositions.add(client.sendData(CompletionPropositionsStorage.getCodeSnippet()));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
            CodePropositionDialogWrapper dialog = new CodePropositionDialogWrapper(propositions, e.getProject(), editor);
            dialog.show();
        }
    }
}
