package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.sample.SamplePages
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ResilientPageRepositoryTest {

    @Test
    fun `remote page is preferred`() = runBlocking {
        var assetReads = 0

        val repository = ResilientPageRepository(
            remoteSource = PageDocumentSource {
                validJson("remote-home")
            },
            assetSource = PageDocumentSource {
                assetReads++
                validJson("asset-home")
            },
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.REMOTE, result.source)
        assertEquals("remote-home", result.page.pageId)
        assertEquals(0, assetReads)
    }

    @Test
    fun `asset is used when remote fails`() = runBlocking {
        val repository = ResilientPageRepository(
            remoteSource = PageDocumentSource {
                error("Network unavailable")
            },
            assetSource = PageDocumentSource {
                validJson("asset-home")
            },
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.ASSET, result.source)
        assertEquals("asset-home", result.page.pageId)
        assertNotNull(result.warning)
    }

    @Test
    fun `asset is used when remote JSON is invalid`() = runBlocking {
        val repository = ResilientPageRepository(
            remoteSource = PageDocumentSource {
                "invalid JSON"
            },
            assetSource = PageDocumentSource {
                validJson("asset-home")
            },
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.ASSET, result.source)
        assertEquals("asset-home", result.page.pageId)
    }

    @Test
    fun `emergency page is used when both sources fail`() = runBlocking {
        val repository = ResilientPageRepository(
            remoteSource = PageDocumentSource {
                error("Remote unavailable")
            },
            assetSource = PageDocumentSource {
                error("Asset unavailable")
            },
            emergencyPage = SamplePages.home,
        )

        val result = repository.loadHomePage()

        assertEquals(PageSource.FALLBACK, result.source)
        assertEquals(SamplePages.home, result.page)
        assertNotNull(result.warning)
    }

    private fun validJson(pageId: String): String {
        return """
            {
              "schemaVersion": 1,
              "pageId": "$pageId",
              "components": [
                {
                  "type": "textCallout",
                  "id": "message",
                  "text": "Loaded successfully"
                }
              ]
            }
        """.trimIndent()
    }
}