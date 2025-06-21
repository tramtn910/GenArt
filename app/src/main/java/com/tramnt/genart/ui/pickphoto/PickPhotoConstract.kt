package com.tramnt.genart.ui.pickphoto

import android.net.Uri
import com.tramnt.genart.base.mvi.MviEffect
import com.tramnt.genart.base.mvi.MviIntent
import com.tramnt.genart.base.mvi.MviViewState

sealed class PickPhotoIntent : MviIntent {
    data object LoadPhotos : PickPhotoIntent()
    data class SelectPhoto(val uri: Uri) : PickPhotoIntent()
    data class PermissionResult(val granted: Boolean) : PickPhotoIntent()
}

data class PickPhotoViewState(
    val photos: List<Uri> = emptyList(),
    val selectedPhoto: Uri? = null,
    val isLoading: Boolean = false,
    val hasPermission: Boolean = false
) : MviViewState

sealed class PickPhotoEffect : MviEffect

