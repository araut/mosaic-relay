package com.araut.mosaicrelay.sdui.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.araut.mosaicrelay.sdui.renderer.SduiRenderer
import com.araut.mosaicrelay.sdui.screen.HomeScreen
import com.slack.circuit.runtime.ui.Ui

data object HomeUi : Ui<HomeScreen.State> {

    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier,
    ) {
        when (state) {
            is HomeScreen.State.Loading -> LoadingContent(modifier)

            is HomeScreen.State.Content -> PageContent(
                state = state,
                modifier = modifier,
            )

            is HomeScreen.State.Error -> ErrorContent(
                state = state,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PageContent(
    state: HomeScreen.State.Content,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        if (state.usingFallback) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                ) {
                    Text(
                        text = "Displaying bundled fallback content",
                        style = MaterialTheme.typography.labelLarge,
                    )

                    state.warning?.let { warning ->
                        Text(
                            text = warning,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }

        SduiRenderer(page = state.page)
    }
}

@Composable
private fun ErrorContent(
    state: HomeScreen.State.Error,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Unable to load the home page",
            style = MaterialTheme.typography.headlineSmall,
        )

        Text(
            text = state.message,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = {
                state.eventSink(HomeScreen.Event.Retry)
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Retry")
        }
    }
}