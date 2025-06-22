package com.tramnt.genart.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tramnt.genart.data.local.entity.HistoryImageEntity

@Composable
fun HistoryScreen(
    historyImages: List<HistoryImageEntity>,
    onDelete: (HistoryImageEntity) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("History", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onClearAll, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Clear All", color = Color.White)
            }
        }
        if (historyImages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No history yet.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(historyImages) { image ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color(0xFFF5F5F5))
                            .clickable { /* TODO: Xem chi tiết ảnh */ },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(image.url),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).padding(8.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(image.prompt, style = MaterialTheme.typography.bodyLarge)
                            Text(image.style ?: "", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { onDelete(image) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
} 