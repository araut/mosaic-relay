package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.model.UiComponent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PageJsonParserTest {

    private val parser = PageJsonParser()

    @Test
    fun `parse creates supported components`() {
        val page = parser.parse(
            """
            {
              "schemaVersion": 1,
              "pageId": "home",
              "components": [
                {
                  "type": "hero",
                  "id": "hero",
                  "title": "Mosaic Relay",
                  "subtitle": "Server-driven UI"
                }
              ]
            }
            """.trimIndent(),
        )

        assertEquals("home", page.pageId)
        assertEquals(1, page.components.size)
        assertTrue(page.components.first() is UiComponent.Hero)
    }

    @Test
    fun `unknown component becomes unsupported`() {
        val page = parser.parse(
            """
            {
              "schemaVersion": 1,
              "pageId": "home",
              "components": [
                {
                  "type": "futureComponent",
                  "id": "future"
                }
              ]
            }
            """.trimIndent(),
        )

        val component = page.components.first()

        assertTrue(component is UiComponent.Unsupported)
        assertEquals(
            "futureComponent",
            (component as UiComponent.Unsupported).originalType,
        )
    }

    @Test(expected = PageValidationException::class)
    fun `duplicate component IDs are rejected`() {
        parser.parse(
            """
            {
              "schemaVersion": 1,
              "pageId": "home",
              "components": [
                {
                  "type": "textCallout",
                  "id": "duplicate",
                  "text": "First"
                },
                {
                  "type": "textCallout",
                  "id": "duplicate",
                  "text": "Second"
                }
              ]
            }
            """.trimIndent(),
        )
    }
}