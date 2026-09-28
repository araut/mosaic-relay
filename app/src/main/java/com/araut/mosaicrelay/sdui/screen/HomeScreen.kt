package com.araut.mosaicrelay.sdui.screen

import android.os.Parcel
import android.os.Parcelable
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data object HomeScreen : Screen {

    override fun describeContents(): Int = 0

    override fun writeToParcel(
        destination: Parcel,
        flags: Int,
    ) = Unit

    @JvmField
    val CREATOR: Parcelable.Creator<HomeScreen> =
        object : Parcelable.Creator<HomeScreen> {

            override fun createFromParcel(
                source: Parcel,
            ): HomeScreen = HomeScreen

            override fun newArray(
                size: Int,
            ): Array<HomeScreen?> = arrayOfNulls(size)
        }

    sealed interface State : CircuitUiState {
        val eventSink: (Event) -> Unit

        data class Loading(
            override val eventSink: (Event) -> Unit,
        ) : State

        data class Content(
            val page: PageDefinition,
            val usingFallback: Boolean,
            val warning: String?,
            override val eventSink: (Event) -> Unit,
        ) : State

        data class Error(
            val message: String,
            override val eventSink: (Event) -> Unit,
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Retry : Event
    }
}