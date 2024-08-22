package org.example.codellamacopilot.completionutil

import com.intellij.codeInsight.inline.completion.DebouncedInlineCompletionProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.InlineCompletionEvent
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.example.codellamacopilot.settings.CopilotSettingsState
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CodeLlamaDebouncedCompletionProvider : DebouncedInlineCompletionProviderRebuild() {
    override val delay: Duration
        get() = 1.seconds

    override fun force(request: InlineCompletionRequest): Boolean {
        return false
    }

    override suspend fun getProposalsDebounced(request: InlineCompletionRequest): Flow<InlineCompletionElement> {
        val inlineCompletionMethods = InlineCompletionMethods(request)
        return inlineCompletionMethods.getProposals()
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return CopilotSettingsState.getInstance().useCompletion;
    }
}