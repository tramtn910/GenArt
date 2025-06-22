package com.tramnt.genart.ui.genart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tramnt.genart.ui.genart.componant.StyleTabComponent

@Composable
fun GenArtScreen(
    state: GenArtViewState,
    onIntent: (GenArtIntent) -> Unit
) {
    LaunchedEffect(Unit) {
        onIntent(GenArtIntent.LoadStyles)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                // Authentication Status Card
                state.authStatus?.let { status ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = status,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onIntent(GenArtIntent.ClearAuthStatus) }) {
                                Text("×", fontSize = 20.sp, color = Color(0xFF2E7D32))
                            }
                        }
                    }
                }

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Test Auth Button
                    Button(
                        onClick = { onIntent(GenArtIntent.TestAuthentication) },
                        enabled = !state.isAuthenticating,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (state.isAuthenticating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Test Auth", color = Color.White)
                        }
                    }

                    // Generate AI Button
                    Button(
                        onClick = { onIntent(GenArtIntent.GenerateAI) },
                        enabled = state.photoUri != null,
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.photoUri != null) Color(0xFFE040FB) else Color(
                                0xFFCCCCCC
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Generate AI Art", color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.prompt,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(2.dp, Color.Magenta, RoundedCornerShape(16.dp)),
                placeholder = { Text("Enter your prompt…") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(2.dp, Color.Magenta, RoundedCornerShape(16.dp))
                    .clickable { onIntent(GenArtIntent.AddPhoto) },
                contentAlignment = Alignment.Center
            ) {
                if (state.photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(state.photoUri),
                        contentDescription = "Selected Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.LightGray
                        )
                        Text("Add your photo", color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            StyleTabComponent(
                state = state,
                onIntent = onIntent
            )
        }
    }
}

@Preview
@Composable
private fun GenArtScreenPreview() {
    GenArtScreen(
        state = GenArtViewState(),
        onIntent = {}
    )
} 