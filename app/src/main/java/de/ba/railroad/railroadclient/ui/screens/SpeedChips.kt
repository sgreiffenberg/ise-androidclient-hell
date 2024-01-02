package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.ba.railroad.railroadclient.model.Direction
import de.ba.railroad.railroadclient.model.Locomotive
import de.ba.railroad.railroadclient.model.RailroadViewModel
import de.ba.railroad.railroadclient.model.Server
import kotlin.math.abs

/**
 * Display a speed setter for the locomotive.
 * Speed is set by multiple FilterChip buttons.
 *
 * @author Steffen Greiffenberg
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SpeedChips(
    locomotive: Locomotive,
    railroadViewModel: RailroadViewModel,
    server: Server
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val speed = locomotive.speed
        val direction = locomotive.direction

        Text(text = "Speed: ")

        val step = 64
        for (speedStep in -256..256 step step) {
            FilterChip(
                onClick = {
                    locomotive.speed = abs(speedStep)
                    locomotive.direction = if (speedStep < 0) {
                        Direction.DIRECTION_BACKWARD
                    } else {
                        Direction.DIRECTION_FORWARD
                    }
                    railroadViewModel.send(locomotive, server)
                },
                label = {
                    Text("")
                },

                selected = speed <= abs(speedStep) && speed > abs(speedStep) - step &&
                        ((direction == Direction.DIRECTION_BACKWARD && speedStep < 0) ||
                                (direction == Direction.DIRECTION_FORWARD && speedStep >= 0)),
                modifier = Modifier.width(24.dp).padding(2.dp).height((24.0 + (abs(speedStep) / 256.0 * 16.0)).dp)

            )
        }
    }
}