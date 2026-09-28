package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.sample.SamplePages
import kotlinx.coroutines.CancellationException

class ResilientPageRepository(
    private val remoteSource: PageDocumentSource,
    private val assetSource: PageDocumentSource,
    private val parser: PageJsonParser = PageJsonParser(),
    private val emergencyPage: PageDefinition = SamplePages.home,
) : PageRepository {

    override suspend fun loadHomePage(): PageLoadResult {
        val remoteAttempt = loadAndParse(remoteSource)

        if (remoteAttempt is Attempt.Success) {
            return PageLoadResult(
                page = remoteAttempt.page,
                source = PageSource.REMOTE,
            )
        }

        val assetAttempt = loadAndParse(assetSource)

        if (assetAttempt is Attempt.Success) {
            return PageLoadResult(
                page = assetAttempt.page,
                source = PageSource.ASSET,
                warning = failureMessage(
                    label = "Remote source",
                    attempt = remoteAttempt,
                ),
            )
        }

        return PageLoadResult(
            page = emergencyPage,
            source = PageSource.FALLBACK,
            warning = listOf(
                failureMessage("Remote source", remoteAttempt),
                failureMessage("Asset source", assetAttempt),
            ).joinToString(separator = "; "),
        )
    }

    private suspend fun loadAndParse(
        source: PageDocumentSource,
    ): Attempt {
        return try {
            Attempt.Success(
                page = parser.parse(source.read()),
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Attempt.Failure(
                message = exception.message
                    ?: exception.javaClass.simpleName,
            )
        }
    }

    private fun failureMessage(
        label: String,
        attempt: Attempt,
    ): String {
        return when (attempt) {
            is Attempt.Failure -> "$label failed: ${attempt.message}"
            is Attempt.Success -> "$label succeeded"
        }
    }

    private sealed interface Attempt {
        data class Success(
            val page: PageDefinition,
        ) : Attempt

        data class Failure(
            val message: String,
        ) : Attempt
    }
}