package org.example.codellamacopilot.completionutil

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSuggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.example.codellamacopilot.settings.CopilotSettingsState
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Debounced Inline completion provider that provides completions for the CodeLlama plugin with a delay.
 */
class CodeLlamaDebouncedCompletionProvider : DebouncedInlineCompletionProviderRebuild() {
    override val id: InlineCompletionProviderID
        get() = InlineCompletionProviderID("CodeLlamaDebouncedCompletionProvider")
    override val delay: Duration
        get() = 0.5.seconds

    override fun force(request: InlineCompletionRequest): Boolean {
        return false
    }

    override suspend fun getProposalsDebounced(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val inlineCompletionMethods = InlineCompletionMethods(request)
        return inlineCompletionMethods.getProposals()
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return CopilotSettingsState.getInstance().useCompletion;
    }
}