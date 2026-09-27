package com.araut.mosaicrelay.sdui.validation

import com.araut.mosaicrelay.sdui.model.ContentItem
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.model.UiComponent
import org.junit.Assert.assertTrue
import org.junit.Test

class PageDefinitionValidatorTest {

    @Test
    fun validPageReturnsNoErrors() {
        val page = PageDefinition(
            schemaVersion = 1,
            pageId = "home",
            components = listOf(
                UiComponent.Hero(
                    id = "hero",
                    title = "Featured",
                    subtitle = "Recommended today",
                ),
            ),
        )

        val errors = PageDefinitionValidator.validate(page)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun unsupportedSchemaVersionReturnsError() {
        val page = PageDefinition(
            schemaVersion = 2,
            pageId = "home",
            components = emptyList(),
        )

        val errors = PageDefinitionValidator.validate(page)

        assertTrue(
            errors.any { error ->
                error.contains("Unsupported schema version")
            },
        )
    }

    @Test
    fun duplicateComponentIdsReturnError() {
        val page = PageDefinition(
            schemaVersion = 1,
            pageId = "home",
            components = listOf(
                UiComponent.TextCallout(
                    id = "message",
                    text = "First message",
                ),
                UiComponent.TextCallout(
                    id = "message",
                    text = "Second message",
                ),
            ),
        )

        val errors = PageDefinitionValidator.validate(page)

        assertTrue(
            errors.any { error ->
                error.contains("Duplicate component ID")
            },
        )
    }

    @Test
    fun duplicateContentItemIdsReturnError() {
        val page = PageDefinition(
            schemaVersion = 1,
            pageId = "home",
            components = listOf(
                UiComponent.ContentRow(
                    id = "row",
                    title = "Popular",
                    items = listOf(
                        ContentItem(
                            id = "item",
                            title = "First",
                        ),
                        ContentItem(
                            id = "item",
                            title = "Second",
                        ),
                    ),
                ),
            ),
        )

        val errors = PageDefinitionValidator.validate(page)

        assertTrue(
            errors.any { error ->
                error.contains("duplicate item ID")
            },
        )
    }

    @Test
    fun unsupportedComponentIsAcceptedAsSafeFallback() {
        val page = PageDefinition(
            schemaVersion = 1,
            pageId = "home",
            components = listOf(
                UiComponent.Unsupported(
                    id = "future-component",
                    originalType = "interactive_game_v2",
                ),
            ),
        )

        val errors = PageDefinitionValidator.validate(page)

        assertTrue(errors.isEmpty())
    }
}