package de.ba.railroad.railroadclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import de.ba.railroad.railroadclient.ui.RailroadApp
import de.ba.railroad.railroadclient.ui.theme.VerySimpleRailroadClientTheme

/**
 * Shows the RailroadApp
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VerySimpleRailroadClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RailroadApp()
                }
            }
        }
    }
}
