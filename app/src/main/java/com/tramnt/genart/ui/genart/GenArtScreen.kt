package com.tramnt.genart.ui.genart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tramnt.genart.ui.genart.componant.StyleTabComponent
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFE040FB), Color(0xFF7C4DFF))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Button(
                        onClick = { onIntent(GenArtIntent.GenerateAI) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues()
                    ) {
                        Text("Generate AI Art", color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = state.prompt,
                    onValueChange = { onIntent(GenArtIntent.UpdatePrompt(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .border(2.dp, Color.Magenta, RoundedCornerShape(16.dp)),
                    placeholder = { Text("Enter your prompt (e.g., yellow hair, chubby, anime girl...)") },
                    singleLine = false,
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        if (state.prompt.isNotEmpty()) {
                            IconButton(onClick = { onIntent(GenArtIntent.UpdatePrompt("")) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear prompt")
                            }
                        }
                    }
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

            if (state.isGenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(enabled = false, onClick = {}),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFE040FB),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Generating...",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
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