package de.ba.railroad.railroadclient.network

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.ba.railroad.railroadclient.model.Locomotive
import de.ba.railroad.railroadclient.model.Server
import de.ba.railroad.railroadclient.ui.screens.LocomotiveUiState
import de.ba.railroad.railroadclient.model.RailroadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * Listener for WebSocket connections.
 * Updates the locomotiveUiStates of the ViewModel.
 *
 * @author Steffen Greiffenberg
 */
class LocomotiveWebSocketListener(
    private val viewModel: RailroadViewModel,
    private val server: Server
) : WebSocketListener() {

    private val TAG = "LocomotiveWebSocketListener"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.locomotiveUiStates[server]?.value = LocomotiveUiState.Connected
        Log.d(TAG, "onOpen:")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        val locomotive = Json.decodeFromString<Locomotive>(text)
        viewModel.locomotiveUiStates[server]?.value =
            LocomotiveUiState.Success(locomotive = MutableStateFlow(locomotive), webSocket = webSocket)

        Log.d(TAG, "onMessage (${server.name}): $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        viewModel.locomotiveUiStates[server]?.value = LocomotiveUiState.Closing
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.locomotiveUiStates[server]?.value = LocomotiveUiState.Closed
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.locomotiveUiStates[server]?.value = LocomotiveUiState.Error
        Log.d(TAG, "onFailure: ${t.message} $response")
    }
}