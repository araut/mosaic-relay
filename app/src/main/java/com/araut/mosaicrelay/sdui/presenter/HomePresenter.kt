package com.araut.mosaicrelay.sdui.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.araut.mosaicrelay.sdui.sample.SamplePages
import com.araut.mosaicrelay.sdui.screen.HomeScreen
import com.slack.circuit.runtime.presenter.Presenter

class HomePresenter(
    private val loadPage: () -> PageDefinition = {
        SamplePages.home
    },
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        var refreshKey by rememberSaveable {
            mutableIntStateOf(0)
        }

        val page = remember(refreshKey) {
            loadPage()
        }

        return HomeScreen.State(
            page = page,
            eventSink = { event ->
                when (event) {
                    HomeScreen.Event.Refresh -> refreshKey++
                }
            },
        )
    }
}