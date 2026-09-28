package com.araut.mosaicrelay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.araut.mosaicrelay.sdui.data.AssetPageRepository
import com.araut.mosaicrelay.sdui.presenter.HomePresenter
import com.araut.mosaicrelay.sdui.screen.HomeScreen
import com.araut.mosaicrelay.sdui.ui.HomeUi
import com.araut.mosaicrelay.ui.theme.MosaicRelayTheme
import com.slack.circuit.foundation.CircuitContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MosaicRelayTheme {
                val repository = remember {
                    AssetPageRepository(
                        readAsset = { filename ->
                            applicationContext.assets
                                .open(filename)
                                .bufferedReader()
                                .use { reader -> reader.readText() }
                        },
                    )
                }

                val presenter = remember(repository) {
                    HomePresenter(repository)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    CircuitContent(
                        screen = HomeScreen,
                        presenter = presenter,
                        ui = HomeUi,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}