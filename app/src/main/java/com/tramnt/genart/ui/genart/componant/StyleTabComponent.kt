package com.tramnt.genart.ui.genart.componant

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tramnt.genart.data.model.Style
import com.tramnt.genart.data.model.StyleCategory
import com.tramnt.genart.ui.genart.GenArtIntent
import com.tramnt.genart.ui.genart.GenArtViewState

@Composable
fun StyleTabComponent(
    state: GenArtViewState,
    onIntent: (GenArtIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Choose your Style",
            color = Color(0xFFE400D9),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Text(
                text = "Error: ${state.error}",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                items(state.styleCategories) { category ->
                    StyleCategoryTab(
                        category = category,
                        isSelected = state.selectedCategory?.id == category.id,
                        onClick = { onIntent(GenArtIntent.SelectStyleCategory(category)) }
                    )
                }
            }
            state.selectedCategory?.let { category ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(category.styles) { style ->
                        StyleItem(
                            style = style,
                            isSelected = state.selectedStyleItem?.id == style.id,
                            onClick = { onIntent(GenArtIntent.SelectStyleItem(style)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StyleCategoryTab(
    category: StyleCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFFE400D9) else Color.Gray
    val textDecoration = if (isSelected) TextDecoration.Underline else null

    Text(
        text = category.name,
        modifier = Modifier.clickable { onClick() },
        color = color,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        fontSize = 14.sp,
        textDecoration = textDecoration
    )
}

@Composable
fun StyleItem(
    style: Style,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, Color(0xFF7F00FF), RoundedCornerShape(12.dp))
    } else {
        Modifier
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = style.key,
            contentDescription = style.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .then(borderModifier)
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = style.name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color(0xFF7F00FF) else Color.Gray
        )
    }
} 