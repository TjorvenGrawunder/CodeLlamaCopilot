package org.example.codellamacopilot.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.example.codellamacopilot.llamaconnection.CompletionClient;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class InsertCodeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        CompletionClient client = new CompletionClient(CopilotSettingsState.getInstance().usedModel);
        Project currentProject = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if(editor != null) {
            Document document = editor.getDocument();
            CaretModel caretModel = editor.getCaretModel();
            int currentLine = caretModel.getVisualLineStart();
            int caretOffset = caretModel.getOffset();
            String prefix = document.getCharsSequence().subSequence(document.getLineStartOffset(0), caretOffset).toString();
            String suffix = document.getCharsSequence().subSequence(caretOffset, document.getTextLength()).toString();

            CodeSnippet code = new CodeSnippet(prefix, suffix);
            if (code != null) {
                String response;
                try {
                    response = client.sendData(code);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                /*WriteCommandAction.runWriteCommandAction(currentProject, () -> {
                    boolean isHighlightingEnabled = editor.getSettings().isLineMarkerAreaShown();
                    editor.getSettings().setLineMarkerAreaShown(false);
                    editor.getMarkupModel().removeAllHighlighters();
                    TextAttributes attributes = new TextAttributes();
                    attributes.setForegroundColor(JBColor.gray);
                    document.insertString(caretOffset, response);
                    editor.getMarkupModel().addRangeHighlighter(caretOffset, caretOffset + response.length(), HighlighterLayer.LAST, attributes, HighlighterTargetArea.EXACT_RANGE);
                    editor.getSettings().setLineMarkerAreaShown(isHighlightingEnabled);
                });
                TypedAction typedAction = TypedAction.getInstance();
                EditorActionManager actionManager = EditorActionManager.getInstance();
                EditorActionHandler actionHandler =
                        actionManager.getActionHandler(IdeActions.ACTION_CODE_COMPLETION);

                typedAction.setupHandler(new AcceptCompletionHandler(response, typedAction.getHandler(), typedAction));
                */
            }
        }
    }
}
