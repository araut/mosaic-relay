package com.araut.mosaicrelay.sdui.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.araut.mosaicrelay.sdui.data.PageLoadResult
import com.araut.mosaicrelay.sdui.data.PageRepository
import com.araut.mosaicrelay.sdui.data.PageSource
import com.araut.mosaicrelay.sdui.screen.HomeScreen
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CancellationException

class HomePresenter(
    private val repository: PageRepository,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        var requestId by rememberSaveable {
            mutableIntStateOf(0)
        }

        var loadState by remember {
            mutableStateOf<LoadState>(LoadState.Loading)
        }

        LaunchedEffect(requestId) {
            loadState = LoadState.Loading

            loadState = try {
                LoadState.Content(repository.loadHomePage())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                LoadState.Error(
                    message = exception.message
                        ?: "The home page could not be loaded",
                )
            }
        }

        val eventSink: (HomeScreen.Event) -> Unit = { event ->
            when (event) {
                HomeScreen.Event.Retry -> requestId++
            }
        }

        return when (val current = loadState) {
            LoadState.Loading -> HomeScreen.State.Loading(
                eventSink = eventSink,
            )

            is LoadState.Content -> HomeScreen.State.Content(
                page = current.result.page,
                usingFallback = current.result.source == PageSource.FALLBACK,
                warning = current.result.warning,
                eventSink = eventSink,
            )

            is LoadState.Error -> HomeScreen.State.Error(
                message = current.message,
                eventSink = eventSink,
            )
        }
    }

    private sealed interface LoadState {
        data object Loading : LoadState

        data class Content(
            val result: PageLoadResult,
        ) : LoadState

        data class Error(
            val message: String,
        ) : LoadState
    }
}