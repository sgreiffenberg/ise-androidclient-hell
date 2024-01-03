package de.ba.railroad.railroadclient.network

import android.util.Log
import de.ba.railroad.railroadclient.model.Server
import de.ba.railroad.railroadclient.ui.screens.LocomotiveUiState
import de.ba.railroad.railroadclient.ui.screens.RailroadUiState
import de.ba.railroad.railroadclient.model.RailroadViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Listener for ReST API calls.
 * Updates railroadUiState ans creates WebSocket connections for
 * each server received from the railroad server.
 *
 * @author Steffen Greiffenberg
 */
class ServerRestAPIListener(private val viewModel: RailroadViewModel) : Callback {

    private val TAG = "ServerRestAPIListener"

    override fun onFailure(call: Call, e: IOException) {
        viewModel.railroadUiState.value = RailroadUiState.Error
        Log.e(TAG, "IOException", e)
    }

    override fun onResponse(call: Call, response: Response) {
        try {
            val responseBody = response.body?.string()

            if (responseBody == null) {
                viewModel.railroadUiState.value = RailroadUiState.Error
                return
            }

            val listResult: List<Server> = Json.decodeFromString(responseBody)

            for (server in listResult) {
                /** The mutable State that stores the status of the most recent request */
                viewModel.locomotiveUiStates[server] = MutableStateFlow(LocomotiveUiState.Closed)

                val request = Request.Builder()
                    .url(server.url)
                    .build()

                viewModel.webSocketClient.newWebSocket(
                    request,
                    LocomotiveWebSocketListener(viewModel, server)
                )
            }

            if (listResult.isNotEmpty()) {
                viewModel.railroadUiState.value = RailroadUiState.Success(listResult, MutableStateFlow(listResult.first()))
                Log.d(TAG, "select server ${listResult.first().name}")
            } else {
                Log.e(TAG, "No servers found")
                viewModel.railroadUiState.value = RailroadUiState.Error
            }

        } catch (t: Throwable) {
            Log.e(TAG, "Exception", t)
            viewModel.railroadUiState.value = RailroadUiState.Error
            RailroadUiState.Error
        }
    }
}
