package com.araut.mosaicrelay.sdui.validation

import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.model.UiComponent

object PageDefinitionValidator {

    const val SUPPORTED_SCHEMA_VERSION = 1
    const val MAX_COMPONENTS = 50
    const val MAX_CONTENT_ROW_ITEMS = 20

    fun validate(page: PageDefinition): List<String> {
        val errors = mutableListOf<String>()

        if (page.schemaVersion != SUPPORTED_SCHEMA_VERSION) {
            errors +=
                "Unsupported schema version ${page.schemaVersion}. " +
                        "Expected $SUPPORTED_SCHEMA_VERSION."
        }

        if (page.pageId.isBlank()) {
            errors += "Page ID must not be blank."
        }

        if (page.components.size > MAX_COMPONENTS) {
            errors +=
                "Page contains ${page.components.size} components. " +
                        "Maximum allowed is $MAX_COMPONENTS."
        }

        val componentIds = mutableSetOf<String>()

        page.components.forEach { component ->
            if (component.id.isBlank()) {
                errors += "Component ID must not be blank."
            } else if (!componentIds.add(component.id)) {
                errors += "Duplicate component ID: ${component.id}."
            }

            when (component) {
                is UiComponent.Hero -> {
                    if (component.title.isBlank()) {
                        errors += "Hero ${component.id} must have a title."
                    }
                }

                is UiComponent.ContentRow -> {
                    if (component.title.isBlank()) {
                        errors += "Content row ${component.id} must have a title."
                    }

                    if (component.items.size > MAX_CONTENT_ROW_ITEMS) {
                        errors +=
                            "Content row ${component.id} contains too many items. " +
                                    "Maximum allowed is $MAX_CONTENT_ROW_ITEMS."
                    }

                    val itemIds = mutableSetOf<String>()

                    component.items.forEach { item ->
                        if (item.id.isBlank()) {
                            errors +=
                                "Content row ${component.id} contains an item " +
                                        "with a blank ID."
                        } else if (!itemIds.add(item.id)) {
                            errors +=
                                "Content row ${component.id} contains duplicate " +
                                        "item ID ${item.id}."
                        }

                        if (item.title.isBlank()) {
                            errors +=
                                "Content item ${item.id} must have a title."
                        }
                    }
                }

                is UiComponent.TextCallout -> {
                    if (component.text.isBlank()) {
                        errors += "Text callout ${component.id} must not be blank."
                    }
                }

                is UiComponent.Unsupported -> {
                    if (component.originalType.isBlank()) {
                        errors +=
                            "Unsupported component ${component.id} must retain " +
                                    "its original type."
                    }
                }
            }
        }

        return errors
    }
}