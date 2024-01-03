package com.tomerpacific.jetpackcomposetabs.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.ba.railroad.railroadclient.model.RailroadViewModel
import de.ba.railroad.railroadclient.ui.screens.ErrorScreen
import de.ba.railroad.railroadclient.ui.screens.LoadingScreen
import de.ba.railroad.railroadclient.ui.screens.LocomotiveUiState
import de.ba.railroad.railroadclient.ui.screens.RailroadUiState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.ba.railroad.railroadclient.model.Locomotive
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
fun LocomotiveTabScreen(
    modifier: Modifier = Modifier,
    railroadUiState: RailroadUiState.Success
) {
    val viewModel: RailroadViewModel = viewModel()

    val server = railroadUiState.selected.collectAsStateWithLifecycle().value
    val servers = railroadUiState.servers
    val selectedTabIndex = max(servers.indexOf(server), 0)

    Column(modifier = modifier) {
        LocomotiveTabs(servers, selectedTabIndex, server) {
            railroadUiState.selected.value = it
        }

        val locomotiveUiState =
            viewModel.locomotiveUiStates[server]?.collectAsStateWithLifecycle()?.value

        LocomotiveUiStateScreen(
            locomotiveUiState = locomotiveUiState,
            modifier = Modifier.padding(16.dp)
        ) {
            // send locomotive changes to the webSocket server
            locomotive -> viewModel.send(locomotive = locomotive)
        }
    }
}

/**
 * The tabs for the screen. Each locomotive has it own tab.
 */
@Composable
private fun LocomotiveTabs(
    servers: List<Server>,
    selectedTabIndex: Int,
    currentServer: Server,
    onTabSelected: (Server) -> Unit
) {
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

@Preview(showBackground = true)
@Composable
fun LocomotiveTabsPreview() {
    val servers = listOf(
        Server(
            name = "Dampflok",
            url = "ws://127.0.0.1/",
            videoURL = "http://127.0.0.1/"
        ), Server(
            name = "Diesellok",
            url = "ws://127.0.0.1/",
            videoURL = "http://127.0.0.1/"
        )
    )

    LocomotiveTabs(
        currentServer = servers.first(),
        servers = servers,
        onTabSelected = { },
        selectedTabIndex = 0
    )
}

/**
 * When a locomotive is selected in the LocomotiveTabs, it will be displayed
 * with this Composable.
 */
@Composable
private fun LocomotiveUiStateScreen(
    locomotiveUiState: LocomotiveUiState?,
    modifier: Modifier,
    onLocomotiveChange: (Locomotive) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (locomotiveUiState) {
            is LocomotiveUiState.Success ->
                LocomotiveScreen(
                    locomotiveUiState = locomotiveUiState,
                    onLocomotiveChange = onLocomotiveChange)

            is LocomotiveUiState.Connecting, is LocomotiveUiState.Connected ->
                LoadingScreen(modifier = Modifier.fillMaxSize())

            is LocomotiveUiState.Error, is LocomotiveUiState.Closing, is LocomotiveUiState.Closed ->
                ErrorScreen(modifier = Modifier.fillMaxSize(), text = "Error")

            else -> ErrorScreen(modifier = Modifier.fillMaxSize(), text = "Unknown state")
        }
    }
}
