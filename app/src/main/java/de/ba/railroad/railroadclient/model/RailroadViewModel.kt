package de.ba.railroad.railroadclient.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ba.railroad.railroadclient.network.ServerRestAPIListener
import de.ba.railroad.railroadclient.ui.screens.LocomotiveUiState
import de.ba.railroad.railroadclient.ui.screens.RailroadUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

/**
 * ViewModel of the App.
 * Holds all LocomotiveWebSocketListeners and UIStates.
 *
 * @author Steffen Greiffenberg
 */
class RailroadViewModel : ViewModel() {

    /** Client for all network requests */
    val webSocketClient = OkHttpClient()
        .newBuilder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    /** Map of all open webSockets */
    var webSockets = mutableMapOf<Server, WebSocket>()

    /** The mutable State that stores the status of the most recent locomotive requests */
    var locomotiveUiStates = mutableMapOf<Server, MutableStateFlow<LocomotiveUiState>>()
        private set

    /** The mutable State that stores the status of the most recent server request */
    var railroadUiState = MutableStateFlow<RailroadUiState>(RailroadUiState.Closed)

    private val defaultServer = Server(
        name = "Dampflok",
        url = "ws://127.0.0.1/",
        videoURL = "http://127.0.0.1/"
    )

    var selectedServer = MutableStateFlow(defaultServer)

    init {
        railroadUiState.value = RailroadUiState.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val request: Request = Request.Builder()
                    .url("http://10.0.2.2:8095/locomotive")
                    .get()
                    .build()
                webSocketClient.newCall(request)
                    .enqueue(ServerRestAPIListener(this@RailroadViewModel))
            }
        }
    }

    /** send locomotive via WebSocket */
    fun send(locomotive: Locomotive, server: Server) {
        val encodedLocomotive = Json.encodeToString(locomotive)
        webSockets[server]?.send(encodedLocomotive)
    }

}