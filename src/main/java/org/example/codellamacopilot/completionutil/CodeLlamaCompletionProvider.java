package org.example.codellamacopilot.completionutil;

import com.intellij.codeInsight.inline.completion.*;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.concurrency.AppExecutorUtil;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import org.example.codellamacopilot.chatwindow.api.ChatClient;
import org.example.codellamacopilot.llamaconnection.CompletionClient;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;
import org.example.codellamacopilot.util.CommentCodeSnippetTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.CancellablePromise;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Deprecated
public class CodeLlamaCompletionProvider implements InlineCompletionProvider {
    @Nullable
    @Override
    public Object getProposals(@NotNull InlineCompletionRequest inlineCompletionRequest, @NotNull Continuation<? super Flow<InlineCompletionElement>> continuation) {
        CompletionClient client = new CompletionClient(CopilotSettingsState.getInstance().getUsedCompletionRequestFormat());
        ChatClient chatClient = new ChatClient(inlineCompletionRequest.getEditor().getProject(), CopilotSettingsState.getInstance().getUsedChatRequestFormat(), false);
        Project currentProject = inlineCompletionRequest.getEditor().getProject();
        String response = "";
        if (currentProject != null) {
            Document document = inlineCompletionRequest.getDocument();
            CaretModel caretModel = inlineCompletionRequest.getEditor().getCaretModel();


            CancellablePromise<CommentCodeSnippetTuple> commentPromise = ReadAction.nonBlocking(() -> {
                ProgressManager.checkCanceled();
                int currentOffset = caretModel.getOffset();
                int currentLine = document.getLineNumber(currentOffset);
                int lineSize = 0;
                boolean foundComment = false;
                String comment = "";
                while (!foundComment && currentLine > 0) {
                    ProgressManager.checkCanceled();
                    comment = document.getCharsSequence().subSequence(document.getLineStartOffset(currentLine), document.getLineEndOffset(currentLine)).toString();
                    if (comment.trim().startsWith("//") || comment.trim().startsWith("/*") || comment.trim().startsWith("/**")) {
                        foundComment = true;
                    } else if (comment.trim().startsWith("*") && comment.trim().endsWith("*/")) {
                        int multiLineCommentEnd = currentLine;
                        while (currentLine > 0 && comment.trim().startsWith("*")) {
                            currentLine--;
                            lineSize++;
                        }
                        comment = document.getCharsSequence().subSequence(document.getLineStartOffset(currentLine), document.getLineEndOffset(multiLineCommentEnd)).toString();
                    } else if (comment.trim().isEmpty()) {
                        currentLine--;
                    } else {
                        comment = "";
                        break;
                    }
                }
                CodeSnippet codeSnippet = new CodeSnippet(document.getCharsSequence().subSequence(document.getLineStartOffset(0), document.getLineStartOffset(currentLine)).toString(),
                        document.getCharsSequence().subSequence(document.getLineStartOffset(currentLine + lineSize), document.getLineEndOffset(document.getLineCount()-1)).toString());
                return new CommentCodeSnippetTuple(comment, codeSnippet);
            }).expireWith(CodeLlamaCopilotPluginDisposable.getInstance()).submit(AppExecutorUtil.getAppExecutorService());

            try {
                if(commentPromise.get() != null && !commentPromise.get().getComment().isEmpty()){
                    try {
                        ProgressManager.checkCanceled();
                        CommentCodeSnippetTuple commentCodeSnippetTuple = commentPromise.get();
                        String message = String.format("""
                                Please implement the \
                                following comment in java. Please pay attention to the given background information.\
                                 Only provide your new code without prefix, suffix and the given comment and dont use markdown. \
                                Comment: %s\s
                                 Prefix: %s\s
                                 Suffix: %s""", commentCodeSnippetTuple.getComment(), commentCodeSnippetTuple.getCodeSnippet().prefix(), commentCodeSnippetTuple.getCodeSnippet().suffix());
                        response = chatClient.sendMessage( message);
                        ProgressManager.checkCanceled();
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    return FlowKt.flowOf(new InlineCompletionElement(response));
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            CancellablePromise<CodeSnippet> cb = ReadAction.nonBlocking(() -> {
                ProgressManager.checkCanceled();
                String codeBefore = document.getCharsSequence().subSequence(document.getLineStartOffset(0), caretModel.getOffset()).toString();
                ProgressManager.checkCanceled();
                String codeAfter = document.getCharsSequence().subSequence(caretModel.getOffset(), document.getTextLength()).toString();

                return new CodeSnippet(codeBefore, codeAfter);
            }).expireWith(CodeLlamaCopilotPluginDisposable.getInstance()).submit(AppExecutorUtil.getAppExecutorService());
            try {
                if (!(cb.get() == null)) {
                    try {
                        ProgressManager.checkCanceled();
                        response = client.sendData(cb.get());
                        ProgressManager.checkCanceled();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return FlowKt.flowOf(new InlineCompletionElement(response));
        }

        return null;
    }

    @Override
    public boolean isEnabled(@NotNull InlineCompletionEvent inlineCompletionEvent) {
        return CopilotSettingsState.getInstance().useCompletion;
    }

    private boolean aboveIsComment(){
        return false;
    }


}
