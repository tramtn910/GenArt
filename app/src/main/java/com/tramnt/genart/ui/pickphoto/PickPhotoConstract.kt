package com.tramnt.genart.ui.pickphoto

import android.net.Uri

sealed class PickPhotoIntent {
    object LoadPhotos : PickPhotoIntent()
    data class SelectPhoto(val uri: Uri) : PickPhotoIntent()
    object Next : PickPhotoIntent()
    object Close : PickPhotoIntent()
}

data class PickPhotoViewState(
    val photos: List<Uri> = emptyList(),
    val selectedPhoto: Uri? = null,
    val isLoading: Boolean = false
)

sealed class PickPhotoEffect {
    object NavigateNext : PickPhotoEffect()
    object CloseScreen : PickPhotoEffect()
}

