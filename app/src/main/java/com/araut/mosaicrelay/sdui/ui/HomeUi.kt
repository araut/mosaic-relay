package com.araut.mosaicrelay.sdui.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.araut.mosaicrelay.sdui.renderer.SduiRenderer
import com.araut.mosaicrelay.sdui.screen.HomeScreen
import com.slack.circuit.runtime.ui.Ui

data object HomeUi : Ui<HomeScreen.State> {

    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            SduiRenderer(page = state.page)
        }
    }
}