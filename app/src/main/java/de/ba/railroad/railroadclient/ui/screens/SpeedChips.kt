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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ba.railroad.railroadclient.model.Direction
import de.ba.railroad.railroadclient.model.Locomotive
import de.ba.railroad.railroadclient.model.RailroadViewModel
import kotlin.math.abs

/**
 * Display a speed setter for the locomotive: Oo-oO
 * Speed is set by multiple FilterChip buttons.
 *
 * @author Steffen Greiffenberg
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SpeedChips(
    locomotive: Locomotive,
    onLocomotiveChange: (Locomotive) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Speed: ")

        val step = 64
        val minSpeed = -256
        val maxSpeed = 256

        for (speedStep in minSpeed..maxSpeed step step) {
            FilterChip(
                onClick = {
                    updateLocomotiveSpeed(locomotive, speedStep)
                    onLocomotiveChange(locomotive)
                },
                label = { Text("") },
                selected = isSelectedSpeed(locomotive.speed, locomotive.direction, speedStep, step),
                modifier = Modifier.getChipModifier(speedStep)
            )
        }
    }
}

/**
 * calculate the size of each chip for speed setting. Larger speed -> larger chip.
 */
private fun Modifier.getChipModifier(speedStep: Int): Modifier {
    val baseSize = 24.dp
    val dynamicSize = (baseSize.value + (abs(speedStep) / 256.0 * 16.0)).dp
    return this
        .width(baseSize)
        .padding(2.dp)
        .height(dynamicSize)
}

/**
 * translate the speed (-256..256) into speed (0..256) and direction (forward..backward)
 */
private fun updateLocomotiveSpeed(locomotive: Locomotive, speedStep: Int) {
    locomotive.speed = abs(speedStep)
    locomotive.direction =
        if (speedStep < 0) Direction.DIRECTION_BACKWARD else Direction.DIRECTION_FORWARD
}

/**
 * Is the speed chip selected? This depends on the current locomotive speed and the
 * speed steps between all chips.
 */
private fun isSelectedSpeed(speed: Int, direction: Direction, speedStep: Int, step: Int): Boolean {
    return speed <= abs(speedStep) && speed > abs(speedStep) - step &&
            ((direction == Direction.DIRECTION_BACKWARD && speedStep < 0) ||
                    (direction == Direction.DIRECTION_FORWARD && speedStep >= 0))
}


@Preview(showBackground = true)
@Composable
fun SpeedChipsPreview() {
    SpeedChips(
        Locomotive(
            name = "Rapunzel",
            isSmoking = true,
            isHeadLight = false,
            isHornSound = false,
            speed = 10,
            direction = Direction.DIRECTION_FORWARD,
            id = "0815"
        )
    ) { /* do nothing on locomotive change */ }
}