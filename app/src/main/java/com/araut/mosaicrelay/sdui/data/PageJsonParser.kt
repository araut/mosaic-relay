package com.araut.mosaicrelay.sdui.data

import com.araut.mosaicrelay.sdui.model.ContentItem
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.model.UiComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PageJsonParser(
    private val json: Json = Json {
        isLenient = false
        ignoreUnknownKeys = true
    },
) {

    fun parse(rawJson: String): PageDefinition {
        val root = json.parseToJsonElement(rawJson).jsonObject

        val page = PageDefinition(
            schemaVersion = root.requiredInt("schemaVersion"),
            pageId = root.requiredString("pageId"),
            components = root.requiredArray("components").map { element ->
                parseComponent(element.jsonObject)
            },
        )

        validate(page)
        return page
    }

    private fun parseComponent(component: JsonObject): UiComponent {
        val type = component.requiredString("type")
        val id = component.requiredString("id")

        return when (type) {
            "hero" -> UiComponent.Hero(
                id = id,
                title = component.requiredString("title"),
                subtitle = component.requiredString("subtitle"),
            )

            "contentRow" -> UiComponent.ContentRow(
                id = id,
                title = component.requiredString("title"),
                items = component.requiredArray("items").map { element ->
                    parseContentItem(element.jsonObject)
                },
            )

            "textCallout" -> UiComponent.TextCallout(
                id = id,
                text = component.requiredString("text"),
            )

            else -> UiComponent.Unsupported(
                id = id,
                originalType = type,
            )
        }
    }

    private fun parseContentItem(item: JsonObject): ContentItem {
        return ContentItem(
            id = item.requiredString("id"),
            title = item.requiredString("title"),
            subtitle = item["subtitle"]
                ?.jsonPrimitive
                ?.contentOrNull,
        )
    }

    private fun validate(page: PageDefinition) {
        val errors = mutableListOf<String>()

        if (page.schemaVersion != SUPPORTED_SCHEMA_VERSION) {
            errors += "Unsupported schema version: ${page.schemaVersion}"
        }

        if (page.pageId.isBlank()) {
            errors += "Page ID must not be blank"
        }

        if (page.components.isEmpty()) {
            errors += "Page must contain at least one component"
        }

        val duplicateComponentIds = page.components
            .groupingBy { it.id }
            .eachCount()
            .filterValues { count -> count > 1 }
            .keys

        if (duplicateComponentIds.isNotEmpty()) {
            errors += "Duplicate component IDs: ${duplicateComponentIds.joinToString()}"
        }

        page.components
            .filterIsInstance<UiComponent.ContentRow>()
            .forEach { row ->
                val duplicateItemIds = row.items
                    .groupingBy { item -> item.id }
                    .eachCount()
                    .filterValues { count -> count > 1 }
                    .keys

                if (duplicateItemIds.isNotEmpty()) {
                    errors +=
                        "Duplicate item IDs in ${row.id}: ${duplicateItemIds.joinToString()}"
                }
            }

        if (errors.isNotEmpty()) {
            throw PageValidationException(errors)
        }
    }

    private fun JsonObject.requiredString(name: String): String {
        return this[name]
            ?.jsonPrimitive
            ?.contentOrNull
            ?.takeIf { value -> value.isNotBlank() }
            ?: throw IllegalArgumentException(
                "Required string '$name' is missing or blank",
            )
    }

    private fun JsonObject.requiredInt(name: String): Int {
        return this[name]
            ?.jsonPrimitive
            ?.intOrNull
            ?: throw IllegalArgumentException(
                "Required integer '$name' is missing",
            )
    }

    private fun JsonObject.requiredArray(name: String): JsonArray {
        return this[name] as? JsonArray
            ?: throw IllegalArgumentException(
                "Required array '$name' is missing",
            )
    }

    private companion object {
        const val SUPPORTED_SCHEMA_VERSION = 1
    }
}

class PageValidationException(
    val errors: List<String>,
) : IllegalArgumentException(errors.joinToString(separator = "; "))