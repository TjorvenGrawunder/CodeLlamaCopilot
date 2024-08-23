package org.example.codellamacopilot.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class AcceptCompletionHandler implements TypedActionHandler {

    private final String TO_INSERT;
    private final TypedActionHandler ORIGINAL_HANDLER;
    private final TypedAction TYPED_ACTION;

    public AcceptCompletionHandler(String toInsert, TypedActionHandler originalHandler, TypedAction typedAction) {
        this.TO_INSERT = toInsert;
        this.ORIGINAL_HANDLER = originalHandler;
        this.TYPED_ACTION = typedAction;
    }
    @Override
    public void execute(@NotNull Editor editor, char charTyped, @NotNull DataContext dataContext) {
        Document document = editor.getDocument();
        int caretOffset = editor.getCaretModel().getOffset();
        editor.getMarkupModel().removeAllHighlighters();
        if(charTyped == '#') {
            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> document.replaceString(caretOffset, caretOffset + TO_INSERT.length(), TO_INSERT));
            editor.getCaretModel().moveToOffset(caretOffset + TO_INSERT.length());
        }else {
            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> document.replaceString(caretOffset, caretOffset + TO_INSERT.length(), String.valueOf(charTyped)));
            editor.getCaretModel().moveToOffset(caretOffset + 1);
        }
        TYPED_ACTION.setupHandler(ORIGINAL_HANDLER);
    }
}
