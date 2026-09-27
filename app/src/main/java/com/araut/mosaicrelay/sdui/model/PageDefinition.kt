package com.araut.mosaicrelay.sdui.model

data class PageDefinition(
    val schemaVersion: Int,
    val pageId: String,
    val components: List<UiComponent>,
    )
