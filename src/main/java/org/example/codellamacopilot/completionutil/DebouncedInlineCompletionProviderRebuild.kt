package org.example.codellamacopilot.completionutil

import com.intellij.codeInsight.inline.completion.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.InlineCompletionProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.job
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

abstract class DebouncedInlineCompletionProviderRebuild : InlineCompletionProvider {
    private var jobCall: Job? = null
    protected abstract val delay: Duration

    /**
     * Returns a Flow of InlineCompletionElement objects with debounced proposals for the given InlineCompletionRequest.
     * Override [delay] to control debounce delay
     */
    abstract suspend fun getProposalsDebounced(request: InlineCompletionRequest): Flow<InlineCompletionElement>

    /**
     * Forces the inline completion for the given request.
     * Might be useful for direct call, since it does not requires any delays
     *
     * @return `true` if the inline completion need to be forced, `false` otherwise.
     */
    abstract fun force(request: InlineCompletionRequest): Boolean

    override suspend fun getProposals(request: InlineCompletionRequest): Flow<InlineCompletionElement> {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return getProposalsDebounced(request)
        }

        if (force(request)) {
            jobCall?.cancel()
            return getProposalsDebounced(request)
        }

        return debounce(request)
    }

    /**
     * Returns a Flow of InlineCompletionElement objects with debounced proposals for the given InlineCompletionRequest.
     */
    private suspend fun debounce(request: InlineCompletionRequest): Flow<InlineCompletionElement> {
        try {
            jobCall?.cancel()
            jobCall = coroutineContext.job
            delay(delay)
            return getProposalsDebounced(request)
        } catch (_: CancellationException) {
            return emptyFlow()
        }
    }
}