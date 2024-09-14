package org.example.codellamacopilot.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.http.annotation.Experimental;
import org.example.codellamacopilot.completionutil.CompletionPropositionsStorage;
import org.example.codellamacopilot.dialogs.CodePropositionDialogWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShowOtherPropositionsAction extends AnAction {

    /**
     * Shows a dialog to choose between other completion propositions
     * @param e AnActionEvent
     */
    @Experimental
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if(!CompletionPropositionsStorage.isEmpty()){
            List<String> propositions = CompletionPropositionsStorage.getPropositions();
            CodePropositionDialogWrapper dialog = new CodePropositionDialogWrapper(propositions);
            dialog.show();
        }
    }
}
