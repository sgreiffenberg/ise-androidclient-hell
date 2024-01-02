package com.tomerpacific.jetpackcomposetabs.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.ba.railroad.railroadclient.model.RailroadViewModel
import de.ba.railroad.railroadclient.ui.screens.ErrorScreen
import de.ba.railroad.railroadclient.ui.screens.LoadingScreen
import de.ba.railroad.railroadclient.ui.screens.LocomotiveUiState
import de.ba.railroad.railroadclient.ui.screens.RailroadUiState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.ba.railroad.railroadclient.ui.screens.LocomotiveScreen

/**
 * Screen to display all available locomotives as TabRow.
 * Each locomotive can be edited in a LocomotiveScreen.
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LocomotiveTabScreen() {

    val viewModel: RailroadViewModel = viewModel()
    val railroadUiState = viewModel.railroadUiState.collectAsStateWithLifecycle().value
    val server = viewModel.selectedServer.collectAsStateWithLifecycle().value

    if (railroadUiState !is RailroadUiState.Success) {
        Text("No connection")
        return
    }
    val servers = railroadUiState.servers

    Column(modifier = Modifier.fillMaxWidth()) {

        var selectedTabIndex = servers.indexOf(server)

        if (selectedTabIndex < 0 && servers.size > 0) {
            selectedTabIndex = 0
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            servers.forEach {
                Tab(text = { Text(it.name) },
                    selected = server == it,
                    onClick = { viewModel.selectedServer.value = it }
                )
            }
        }

        val locomotiveUiState = viewModel.locomotiveUiStates[server]?.collectAsStateWithLifecycle()?.value

        Column(
            // modifier = Modifier.fillMaxSize(),
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (locomotiveUiState) {
                is LocomotiveUiState.Success -> LocomotiveScreen()

                is LocomotiveUiState.Connecting -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is LocomotiveUiState.Connected -> LoadingScreen(modifier = Modifier.fillMaxSize())

                is LocomotiveUiState.Error -> ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    text = "Error connecting locomotive server!"
                )

                is LocomotiveUiState.Closing -> ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    text = "Closing server connection!"
                )

                is LocomotiveUiState.Closed -> ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    text = "Connection to locomotive server closed!"
                )

                else -> ErrorScreen(modifier = Modifier.fillMaxSize(), text = "Unknown state")
            }
        }
    }
}