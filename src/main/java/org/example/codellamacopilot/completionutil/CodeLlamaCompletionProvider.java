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
import org.example.codellamacopilot.llamaconnection.LLMClient;
import org.example.codellamacopilot.settings.CopilotSettingsState;
import org.example.codellamacopilot.util.CodeSnippet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.CancellablePromise;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CodeLlamaCompletionProvider implements InlineCompletionProvider {
    @Nullable
    @Override
    public Object getProposals(@NotNull InlineCompletionRequest inlineCompletionRequest, @NotNull Continuation<? super Flow<InlineCompletionElement>> continuation) {
        LLMClient client = new LLMClient(CopilotSettingsState.getInstance().usedModel);
        Project currentProject = inlineCompletionRequest.getEditor().getProject();
        String response = "";
        if (currentProject != null) {
            Document document = inlineCompletionRequest.getDocument();
            CaretModel caretModel = inlineCompletionRequest.getEditor().getCaretModel();

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


}
