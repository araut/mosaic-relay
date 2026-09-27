package com.araut.mosaicrelay.sdui.sample

import com.araut.mosaicrelay.sdui.model.ContentItem
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.model.UiComponent

object SamplePages {

    val home = PageDefinition(
        schemaVersion = 1,
        pageId = "home",
        components = listOf(
            UiComponent.Hero(
                id = "featured",
                title = "MosaicRelay",
                subtitle =
                    "The server selects approved components. " +
                            "The Android client controls rendering and behavior.",
            ),
            UiComponent.ContentRow(
                id = "popular-now",
                title = "Popular Now",
                items = listOf(
                    ContentItem(
                        id = "item-1",
                        title = "Live Events",
                        subtitle = "Dynamic updates",
                    ),
                    ContentItem(
                        id = "item-2",
                        title = "Movies",
                        subtitle = "Reusable components",
                    ),
                    ContentItem(
                        id = "item-3",
                        title = "Games",
                        subtitle = "Versioned contracts",
                    ),
                ),
            ),
            UiComponent.TextCallout(
                id = "engineering-message",
                text =
                    "Change the order of these definitions and the screen " +
                            "will change without modifying its Compose layout.",
            ),
            UiComponent.Unsupported(
                id = "future-component",
                originalType = "interactive_game_v2",
            ),
        ),
    )
}