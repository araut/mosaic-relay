package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.sample.SamplePages
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetPageRepository(
    private val readAsset: (String) -> String,
    private val parser: PageJsonParser = PageJsonParser(),
    private val fallbackPage: PageDefinition = SamplePages.home,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PageRepository {

    override suspend fun loadHomePage(): PageLoadResult {
        return withContext(dispatcher) {
            try {
                val rawJson = readAsset(HOME_ASSET)
                val page = parser.parse(rawJson)

                PageLoadResult(
                    page = page,
                    source = PageSource.ASSET,
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                PageLoadResult(
                    page = fallbackPage,
                    source = PageSource.FALLBACK,
                    warning = exception.message
                        ?: "The JSON page definition could not be loaded",
                )
            }
        }
    }

    private companion object {
        const val HOME_ASSET = "home.json"
    }
}