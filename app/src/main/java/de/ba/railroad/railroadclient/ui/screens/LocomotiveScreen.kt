package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.ba.railroad.railroadclient.model.Locomotive
import de.ba.railroad.railroadclient.model.RailroadViewModel

/**
 * Screen to display one locomotive
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LocomotiveScreen() {
    val viewModel: RailroadViewModel = viewModel()
    val railroadUiState = viewModel.railroadUiState.collectAsStateWithLifecycle().value

    when (railroadUiState) {
        is RailroadUiState.Success -> {
            val server = railroadUiState.selected.collectAsStateWithLifecycle().value
            val locomotiveUiState = viewModel.locomotiveUiStates[server]?.collectAsStateWithLifecycle()?.value

            if (locomotiveUiState is LocomotiveUiState.Success) {
                LocomotiveContent(
                    locomotiveUiState.locomotive.collectAsStateWithLifecycle().value,
                    viewModel
                )
            } else {
                Text(text = "No connection to locomotive server!")
            }
        }
        else -> Text(text = "No connection to railroad server!")
    }
}

@Composable
private fun LocomotiveContent(locomotive: Locomotive, viewModel: RailroadViewModel) {
    Column {
        Text(text = locomotive.name)
        Text(text = locomotive.number.toString())
        Text(text = "${locomotive.id}")

        LocomotiveFeatureCheckbox("Cabin Lighting", locomotive.isCabinLighting) {
            locomotive.isCabinLighting = it
            viewModel.send(locomotive)
        }

        LocomotiveFeatureCheckbox("Head Light", locomotive.isHeadLight) {
            locomotive.isHeadLight = it
            viewModel.send(locomotive)
        }

        LocomotiveFeatureCheckbox("Smoking", locomotive.isSmoking) {
            locomotive.isSmoking = it
            viewModel.send(locomotive)
        }

        SpeedChips(locomotive, viewModel)
    }
}

@Composable
private fun LocomotiveFeatureCheckbox(text: String, checked: Boolean?, onCheckedChange: (Boolean) -> Unit) {
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
    LocomotiveScreen()
}