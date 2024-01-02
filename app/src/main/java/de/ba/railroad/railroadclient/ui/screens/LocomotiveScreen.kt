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
import de.ba.railroad.railroadclient.model.RailroadViewModel

/**
 * Screen to display one locomotive
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LocomotiveScreen() {
    val viewModel: RailroadViewModel = viewModel()

    val server = viewModel.selectedServer.collectAsStateWithLifecycle().value
    val uiState = viewModel.locomotiveUiStates[server]?.collectAsStateWithLifecycle()?.value

    if (uiState !is LocomotiveUiState.Success) {
        Text(text = "No locomotive!")
        return
    }

    val locomotive = uiState.locomotive.collectAsStateWithLifecycle().value

    Column {
        Text(text = locomotive.name)
        Text(text = locomotive.number.toString())
        Text(text = "${locomotive.id}")

        if (locomotive.isCabinLighting != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = locomotive.isCabinLighting!!, onCheckedChange = {
                    locomotive.isCabinLighting = it
                    viewModel.send(locomotive, server)
                })
                Text(text = "Cabine Lighting")
            }
        }
        if (locomotive.isHeadLight != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = locomotive.isHeadLight!!, onCheckedChange = {
                    locomotive.isHeadLight = it
                    viewModel.send(locomotive, server)
                })
                Text(text = "Head Light")
            }
        }
        if (locomotive.isSmoking != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = locomotive.isSmoking!!, onCheckedChange = {
                    locomotive.isSmoking = it
                    viewModel.send(locomotive, server)
                })
                Text(text = "Smoking")
            }
        }

        SpeedChips(locomotive, viewModel, server)
    }
}

@Preview(showBackground = true)
@Composable
fun LocomotiveScreenPreview() {
    LocomotiveScreen()
}