package com.araut.mosaicrelay.sdui.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetPageSource(
    private val readAsset: (String) -> String,
    private val filename: String = "home.json",
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PageDocumentSource {

    override suspend fun read(): String {
        return withContext(dispatcher) {
            readAsset(filename)
        }
    }
}