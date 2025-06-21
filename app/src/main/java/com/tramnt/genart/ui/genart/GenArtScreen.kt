package com.tramnt.genart.ui.genart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE400D9),
                                Color(0xFF1D00F5)
                            )
                        )
                    )
                    .clickable { onIntent(GenArtIntent.GenerateAI) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Generate AI",
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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