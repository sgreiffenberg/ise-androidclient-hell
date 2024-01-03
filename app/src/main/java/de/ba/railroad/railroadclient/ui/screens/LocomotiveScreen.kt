package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.ba.railroad.railroadclient.model.Direction
import de.ba.railroad.railroadclient.model.Locomotive

/**
 * Screen to display one locomotive
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LocomotiveScreen(
    locomotiveUiState: LocomotiveUiState.Success,
    onLocomotiveChange: (Locomotive) -> Unit
) {
    LocomotiveContent(
        locomotiveUiState.locomotive.collectAsStateWithLifecycle().value,
        onLocomotiveChange)
}

@Composable
private fun LocomotiveContent(
    locomotive: Locomotive,
    onLocomotiveChange: (Locomotive) -> Unit
) {
    Column {
        Text(text = locomotive.name)
        Text(text = locomotive.number.toString())
        Text(text = "${locomotive.id}")

        LocomotiveFeatureCheckbox("Cabin Lighting", locomotive.isCabinLighting) {
            locomotive.isCabinLighting = it
            onLocomotiveChange(locomotive)
        }

        LocomotiveFeatureCheckbox("Head Light", locomotive.isHeadLight) {
            locomotive.isHeadLight = it
            onLocomotiveChange(locomotive)
        }

        LocomotiveFeatureCheckbox("Smoking", locomotive.isSmoking) {
            locomotive.isSmoking = it
            onLocomotiveChange(locomotive)
        }

        SpeedChips(locomotive, onLocomotiveChange)
    }
}

@Composable
private fun LocomotiveFeatureCheckbox(
    text: String,
    checked: Boolean?,
    onCheckedChange: (Boolean) -> Unit
) {
    checked?.let {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = it, onCheckedChange = onCheckedChange)
            Text(text = text)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LocomotiveScreenPreview() {
    LocomotiveContent(
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