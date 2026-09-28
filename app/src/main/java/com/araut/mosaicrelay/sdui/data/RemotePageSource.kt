package com.araut.mosaicrelay.sdui.data

import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class RemotePageSource(
    private val client: OkHttpClient,
    private val url: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PageDocumentSource {

    override suspend fun read(): String {
        return withContext(dispatcher) {
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException(
                        "Remote page request failed with HTTP ${response.code}",
                    )
                }

                response.body.string()
            }
        }
    }
}