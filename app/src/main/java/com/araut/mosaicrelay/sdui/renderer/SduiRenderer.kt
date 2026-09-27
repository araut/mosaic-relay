package com.araut.mosaicrelay.sdui.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.araut.mosaicrelay.sdui.components.ContentRowComponent
import com.araut.mosaicrelay.sdui.components.HeroComponent
import com.araut.mosaicrelay.sdui.components.TextCalloutComponent
import com.araut.mosaicrelay.sdui.components.UnsupportedComponent
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.model.UiComponent
import com.araut.mosaicrelay.sdui.validation.PageDefinitionValidator

@Composable
fun SduiRenderer(
    page: PageDefinition,
    modifier: Modifier = Modifier,
) {
    val validationErrors = remember(page) {
        PageDefinitionValidator.validate(page)
    }

    if (validationErrors.isNotEmpty()) {
        InvalidPage(
            errors = validationErrors,
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = page.components,
            key = { component -> component.id },
        ) { component ->
            when (component) {
                is UiComponent.Hero ->
                    HeroComponent(component)

                is UiComponent.ContentRow ->
                    ContentRowComponent(component)

                is UiComponent.TextCallout ->
                    TextCalloutComponent(component)

                is UiComponent.Unsupported ->
                    UnsupportedComponent(component)
            }
        }
    }
}

@Composable
private fun InvalidPage(
    errors: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Unable to render page",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error,
        )

        errors.forEach { error ->
            Text(
                text = "• $error",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}