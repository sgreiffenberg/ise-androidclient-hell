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
import de.ba.railroad.railroadclient.model.Server
import de.ba.railroad.railroadclient.ui.screens.LocomotiveScreen
import kotlin.math.max

/**
 * Screen to display all available locomotives as TabRow.
 * Each locomotive can be edited in a LocomotiveScreen.
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LocomotiveTabScreen(modifier: Modifier = Modifier) {
    val viewModel: RailroadViewModel = viewModel()
    val railRoadState = viewModel.railroadUiState.collectAsStateWithLifecycle().value

    if (railRoadState !is RailroadUiState.Success) {
        Text(text = "No locomotive!")
        return
    }

    val server = railRoadState.selected.collectAsStateWithLifecycle().value
    val servers = railRoadState.servers
    val selectedTabIndex = max(servers.indexOf(server), 0)

    Column(modifier = modifier) {
        LocomotiveTabs(servers, selectedTabIndex, server) {
            railRoadState.selected.value = it
        }

        val locomotiveUiState =
            viewModel.locomotiveUiStates[server]?.collectAsStateWithLifecycle()?.value

        LocomotiveUiStateScreen(locomotiveUiState, Modifier.padding(16.dp))
    }
}

/**
 * The tabs for the screen. Each locomotive has it own tab.
 */
@Composable
private fun LocomotiveTabs(servers: List<Server>, selectedTabIndex: Int, currentServer: Server, onTabSelected: (Server) -> Unit) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        servers.forEach { server ->
            Tab(
                text = { Text(server.name) },
                selected = currentServer == server,
                onClick = { onTabSelected(server) }
            )
        }
    }
}

/**
 * When a locomotive is selected in the LocomotiveTabs, it will be displayed
 * with this Composable.
 */
@Composable
private fun LocomotiveUiStateScreen(state: LocomotiveUiState?, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is LocomotiveUiState.Success -> LocomotiveScreen()
            is LocomotiveUiState.Connecting, is LocomotiveUiState.Connected ->
                LoadingScreen(modifier = Modifier.fillMaxSize())
            is LocomotiveUiState.Error, is LocomotiveUiState.Closing, is LocomotiveUiState.Closed ->
                ErrorScreen(modifier = Modifier.fillMaxSize(), text = "Error")
            else -> ErrorScreen(modifier = Modifier.fillMaxSize(), text = "Unknown state")
        }
    }
}
