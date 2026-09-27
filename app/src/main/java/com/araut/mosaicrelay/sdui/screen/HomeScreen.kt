package com.araut.mosaicrelay.sdui.screen

import android.os.Parcel
import android.os.Parcelable
import com.araut.mosaicrelay.sdui.model.PageDefinition
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data object HomeScreen : Screen {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    @JvmField
    val CREATOR: Parcelable.Creator<HomeScreen> = object : Parcelable.Creator<HomeScreen> {
        override fun createFromParcel(parcel: Parcel): HomeScreen = HomeScreen
        override fun newArray(size: Int): Array<HomeScreen?> = arrayOfNulls(size)
    }

    data class State(
        val page: PageDefinition,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Refresh : Event
    }
}