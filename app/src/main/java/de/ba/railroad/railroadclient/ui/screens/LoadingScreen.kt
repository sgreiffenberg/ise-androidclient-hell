package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ba.railroad.railroadclient.R
import de.ba.railroad.railroadclient.ui.theme.VerySimpleRailroadClientTheme

/**
 * The home screen displaying the loading message.
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    VerySimpleRailroadClientTheme {
        LoadingScreen()
    }
}