package de.ba.railroad.railroadclient.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private class DirectionSerializer : EnumAsIntSerializer<Direction>(
    "Direction",
    { it.value },
    { v -> Direction.values().first { it.value == v } }
)

@Serializable(with = DirectionSerializer::class)
enum class Direction(val value: Int) {
    DIRECTION_FORWARD(0),
    DIRECTION_BACKWARD(1)
}

/**
 * Locomotive data, can be received from a locomotive server.
 */
@Serializable
class Locomotive(
    @SerialName(value = "id")
    var id: String? = null,
    @SerialName(value = "speed")
    var speed: Int,
    @SerialName(value = "direction")
    var direction: Direction,
    @SerialName(value = "number")
    var number: String? = null,
    @SerialName(value = "name")
    var name: String,
    @SerialName(value = "headLight")
    var isHeadLight: Boolean? = null,
    @SerialName(value = "cabineLighting")
    var isCabinLighting: Boolean? = null,
    @SerialName(value = "hornSound")
    var isHornSound: Boolean? = null,
    @SerialName(value = "drivingSound")
    var isDrivingSound: Boolean? = null,
    @SerialName(value = "smoking")
    var isSmoking: Boolean? = null,
    @SerialName(value = "position")
    var position: Int? = null
)
