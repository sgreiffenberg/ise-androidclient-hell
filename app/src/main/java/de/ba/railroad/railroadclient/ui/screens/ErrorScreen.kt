package de.ba.railroad.railroadclient.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ba.railroad.railroadclient.R
import de.ba.railroad.railroadclient.ui.theme.VerySimpleRailroadClientTheme

/**
 * The home screen displaying an error message as icon.
 *
 * @author Steffen Greiffenberg
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier, text:String = stringResource(R.string.loading_failed)) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = text, modifier = Modifier.padding(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    VerySimpleRailroadClientTheme {
        ErrorScreen()
    }
}
