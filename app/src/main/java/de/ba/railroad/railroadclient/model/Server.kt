package de.ba.railroad.railroadclient.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server data received from a railroad server.
 *
 * @author Steffen Greiffenberg
 */
@Serializable
class Server(
    @SerialName(value = "url")
    val url: String,
    @SerialName(value = "videoURL")
    val videoURL: String,
    @SerialName(value = "name")
    val name: String
)
