package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.runtime.MutableState
import de.ba.railroad.railroadclient.model.Locomotive
import de.ba.railroad.railroadclient.model.Server
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.WebSocket

/**
 * UI state for the main screen
 */
sealed interface RailroadUiState {
    data class Success(
        val servers: List<Server>,
        val selected: MutableStateFlow<Server>
    ) : RailroadUiState

    object Closed : RailroadUiState
    object Error : RailroadUiState
    object Loading : RailroadUiState
}

/**
 * UI state for a locomotive
 */
sealed interface LocomotiveUiState {
    data class Success(
        val locomotive: MutableStateFlow<Locomotive>,
        val webSocket: WebSocket
    ) : LocomotiveUiState

    object Error : LocomotiveUiState
    object Connected : LocomotiveUiState
    object Connecting : LocomotiveUiState
    object Closing : LocomotiveUiState
    object Closed : LocomotiveUiState
}