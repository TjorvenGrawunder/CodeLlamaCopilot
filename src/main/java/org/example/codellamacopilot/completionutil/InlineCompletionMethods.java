package org.example.codellamacopilot.completionutil;

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement;
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement;
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion;
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSuggestion;
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionVariant;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.util.concurrency.AppExecutorUtil;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import org.assertj.core.util.Throwables;
import org.example.codellamacopilot.chatwindow.api.ChatClient;
import org.example.codellamacopilot.dialogs.StackTraceDialogWrapper;
import org.example.codellamacopilot.exceptions.CompletionFailedException;
import org.example.codellamacopilot.exceptions.ErrorMessageException;
import org.example.codellamacopilot.llamaconnection.CompletionClient;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;
import org.example.codellamacopilot.util.CommentCodeSnippetTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.CancellablePromise;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class InlineCompletionMethods {
    private final InlineCompletionRequest INLINE_COMPLETION_REQUEST;
    public InlineCompletionMethods(InlineCompletionRequest inlineCompletionRequest) {
        this.INLINE_COMPLETION_REQUEST = inlineCompletionRequest;
    }

    public InlineCompletionSuggestion getProposals() {
        CompletionClient client = new CompletionClient(CopilotSettingsState.getInstance().getUsedCompletionRequestFormat());
        ChatClient chatClient = new ChatClient(INLINE_COMPLETION_REQUEST.getEditor().getProject(), CopilotSettingsState.getInstance().getUsedChatRequestFormat(false), false);
        Project currentProject = INLINE_COMPLETION_REQUEST.getEditor().getProject();
        String response = "";
        if (currentProject != null) {
            Document document = INLINE_COMPLETION_REQUEST.getDocument();
            CaretModel caretModel = INLINE_COMPLETION_REQUEST.getEditor().getCaretModel();


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
                CommentCodeSnippetTuple commentCodeSnippetTuple = commentPromise.get();
                if(commentCodeSnippetTuple != null && !commentCodeSnippetTuple.getComment().isEmpty()){
                    ProgressManager.checkCanceled();
                    String message = String.format("""
                            Please implement the \
                            following comment in java and fill the gap between prefix and suffix. \
                            Please pay attention to the given background information.\
                             Only provide your new code without prefix, suffix, the given comment and you are not allowed to create a new class. \
                             Dont use Markdown.
                            Comment: %s\s
                             Prefix: %s\s
                             Suffix: %s""", commentCodeSnippetTuple.getComment(), commentCodeSnippetTuple.getCodeSnippet().prefix(), commentCodeSnippetTuple.getCodeSnippet().suffix());
                    response = chatClient.sendMessage( message);
                    ProgressManager.checkCanceled();
                    Flow<InlineCompletionElement> flow = FlowKt.flowOf(new InlineCompletionGrayTextElement(response));
                    return InlineCompletionSingleSuggestion.Companion.build(new UserDataHolderBase(), flow);
                }
            } catch (InterruptedException | ExecutionException | IOException | ErrorMessageException e) {
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
                        long startTime = System.currentTimeMillis();
                        response = client.sendData(cb.get());
                        long endTime = System.currentTimeMillis();
                        System.out.println("Time taken: " + (endTime - startTime) + "ms");
                        ProgressManager.checkCanceled();
                    } catch (CompletionFailedException e) {
                        response = "";
                        String stackTrace = Throwables.getStackTrace(e);
                        StackTraceDialogWrapper stackTraceDialogWrapper = new StackTraceDialogWrapper(stackTrace);
                        stackTraceDialogWrapper.showAndGet();
                    }
                    catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            Flow<InlineCompletionElement> flow = FlowKt.flowOf(new InlineCompletionGrayTextElement(response));
            return InlineCompletionSingleSuggestion.Companion.build(new UserDataHolderBase(), flow);
        }

        return null;
    }
}
