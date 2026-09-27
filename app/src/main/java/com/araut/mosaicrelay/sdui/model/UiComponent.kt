package com.araut.mosaicrelay.sdui.model

sealed interface UiComponent {
    val id: String

    data class Hero(
        override val id: String,
        val title: String,
        val subtitle: String,
    ) : UiComponent

    data class ContentRow(
        override val id: String,
        val title: String,
        val items: List<ContentItem>,
    ) : UiComponent

    data class TextCallout(
        override val id: String,
        val text: String,
    ) : UiComponent

    data class Unsupported(
        override val id: String,
        val originalType: String,
    ) : UiComponent
}

data class ContentItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
)