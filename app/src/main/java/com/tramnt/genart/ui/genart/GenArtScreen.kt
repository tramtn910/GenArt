package com.tramnt.genart.ui.genart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GenArtScreen(
    state: GenArtViewState,
    onIntent: (GenArtIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Prompt box
        OutlinedTextField(
            value = state.prompt,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Magenta, RoundedCornerShape(16.dp)),
            placeholder = { Text("Enter your prompt…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Photo box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(2.dp, Color.Magenta, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
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
        Spacer(modifier = Modifier.height(24.dp))
        // Style selector
        Text(
            text = "Choose your Style",
            color = Color(0xFFD100A4),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        // Tab style
        Row(modifier = Modifier.padding(top = 4.dp)) {
            Text("Trending", color = Color(0xFFD100A4), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Fashion", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Anime", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Digital Art", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Painting", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    ) {
                        // Placeholder for image
                    }
                    Text("Novelistic", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        // Generate button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    listOf(Color(0xFFD100A4), Color(0xFF7F7FFF))
                ).toBrushColor()
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Generate AI", fontWeight = FontWeight.Bold)
        }
    }
}

// Helper extension for gradient button color
fun androidx.compose.ui.graphics.Brush.toBrushColor(): Color = Color.Unspecified

@Preview
@Composable
private fun GenArtScreenPreview() {
    GenArtScreen(
        state = GenArtViewState(),
        onIntent = {}
    )
} 