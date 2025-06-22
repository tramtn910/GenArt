package com.tramnt.genart.ui.pickphoto

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.tramnt.genart.R
import com.tramnt.genart.util.ImageUtils
import kotlinx.coroutines.flow.Flow
import androidx.compose.foundation.lazy.grid.items

@Composable
fun PickPhotoScreen(
    state: PickPhotoViewState,
    onIntent: (PickPhotoIntent) -> Unit,
    onRequestPermission: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            TextButton(
                onClick = onNextClick,
                enabled = state.selectedPhoto != null
            ) {
                Text("Next")
            }
        }

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            !state.hasPermission -> {
                PermissionRequiredContent(onRequestPermission)
            }
            state.photos.isEmpty() -> {
                EmptyPhotosContent()
            }
            else -> {
                PhotosGrid(state, onIntent)
            }
        }
    }
}

@Composable
private fun PermissionRequiredContent(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isModernAndroid = ImageUtils.isModernAndroid()
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Permission Required",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Text(
                text = if (isModernAndroid) "Photo Access Required" else "Storage Permission Required",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = if (isModernAndroid) {
                    "This app needs access to your photos to let you select an image for style transfer. Please grant permission when prompted."
                } else {
                    "This app needs access to your device storage to let you select an image for style transfer. Please grant permission when prompted."
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Button(
                onClick = onRequestPermission
            ) {
                Text("Request Permission")
            }
        }
    }
}

@Composable
private fun EmptyPhotosContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "No Photos",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Text(
                text = "No Photos Found",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "No photos were found on your device.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
private fun PhotosGrid(
    state: PickPhotoViewState,
    onIntent: (PickPhotoIntent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.photos) { uri ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onIntent(PickPhotoIntent.SelectPhoto(uri)) }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                val iconPainter = if (state.selectedPhoto == uri) {
                    painterResource(id = R.drawable.ic_select)
                } else {
                    painterResource(id = R.drawable.ic_unselected)
                }

                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PickPhotoScreenPaging(
    photoPagingFlow: Flow<PagingData<Uri>>,
    selectedPhoto: Uri?,
    onPhotoClick: (Uri) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val photos: LazyPagingItems<Uri> = photoPagingFlow.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            TextButton(
                onClick = onNextClick,
                enabled = selectedPhoto != null
            ) {
                Text("Next")
            }
        }

        when (photos.loadState.refresh) {
            is LoadState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error loading photos")
                }
            }
            else -> {
                if (photos.itemCount == 0) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No Photos Found")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(photos.itemCount) { index ->
                            val uri = photos[index]
                            if (uri != null) {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onPhotoClick(uri) }
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    val iconPainter = if (selectedPhoto == uri) {
                                        painterResource(id = R.drawable.ic_select)
                                    } else {
                                        painterResource(id = R.drawable.ic_unselected)
                                    }
                                    Image(
                                        painter = iconPainter,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PickPhotoScreenPreview() {
    PickPhotoScreen(
        state = PickPhotoViewState(
            photos = listOf(),
            selectedPhoto = null,
            isLoading = false,
            hasPermission = true
        ),
        onIntent = {/* no-op */},
        onRequestPermission = {/* no-op */},
        onBackClick = {/* no-op */},
        onNextClick = {/* no-op */}
    )
}