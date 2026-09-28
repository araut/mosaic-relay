package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.model.PageDefinition

interface PageRepository {
    suspend fun loadHomePage(): PageLoadResult
}

data class PageLoadResult(
    val page: PageDefinition,
    val source: PageSource,
    val warning: String? = null,
)

enum class PageSource {
    REMOTE,
    ASSET,
    FALLBACK,
}