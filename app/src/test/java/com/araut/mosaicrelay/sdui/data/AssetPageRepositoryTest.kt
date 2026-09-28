package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.sample.SamplePages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AssetPageRepositoryTest {

    @Test
    fun `valid asset returns asset page`() = runBlocking {
        val repository = AssetPageRepository(
            readAsset = {
                """
                {
                  "schemaVersion": 1,
                  "pageId": "asset-home",
                  "components": [
                    {
                      "type": "textCallout",
                      "id": "message",
                      "text": "Loaded from JSON"
                    }
                  ]
                }
                """.trimIndent()
            },
            dispatcher = Dispatchers.Unconfined,
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.ASSET, result.source)
        assertEquals("asset-home", result.page.pageId)
    }

    @Test
    fun `invalid asset returns fallback page`() = runBlocking {
        val repository = AssetPageRepository(
            readAsset = { "invalid JSON" },
            fallbackPage = SamplePages.home,
            dispatcher = Dispatchers.Unconfined,
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.FALLBACK, result.source)
        assertEquals(SamplePages.home, result.page)
        assertNotNull(result.warning)
    }
}