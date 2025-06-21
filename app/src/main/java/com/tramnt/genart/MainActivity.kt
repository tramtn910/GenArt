package com.tramnt.genart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tramnt.genart.ui.theme.GenArtTheme
import com.tramnt.genart.ui.genart.GenArtActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GenArtTheme {
                MainScreen(
                    onNavigateToGenArt = {
                        startActivity(Intent(this, GenArtActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToGenArt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GenArt App",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onNavigateToGenArt,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Open GenArt")
        }
    }
} 