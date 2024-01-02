package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomerpacific.jetpackcomposetabs.ui.view.LocomotiveTabScreen
import de.ba.railroad.railroadclient.model.RailroadViewModel

/**
 * Shows all railroad components.
 * The contents depends on the RailroadUiState
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val railroadViewModel: RailroadViewModel = viewModel()

    val railroadUiState = railroadViewModel.railroadUiState.collectAsStateWithLifecycle().value

    when (railroadUiState) {
        is RailroadUiState.Closed -> LoadingScreen(modifier = modifier.fillMaxSize())
        is RailroadUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is RailroadUiState.Success -> RailRoadScreen(modifier = modifier.fillMaxWidth())
        is RailroadUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun RailRoadScreen(modifier: Modifier = Modifier) {
    Column() {
        LocomotiveTabScreen()
    }
}

