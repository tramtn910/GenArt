package com.tramnt.genart.ui.pickphoto

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PickPhotoViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(PickPhotoViewState(isLoading = true))
    val state: StateFlow<PickPhotoViewState> = _state.asStateFlow()

    private val _effect = Channel<PickPhotoEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: PickPhotoIntent) {
        when (intent) {
            is PickPhotoIntent.LoadPhotos -> loadPhotos()
            is PickPhotoIntent.SelectPhoto -> selectPhoto(intent.uri)
            is PickPhotoIntent.Next -> goNext()
            is PickPhotoIntent.Close -> close()
        }
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val uris = getAllImages(getApplication<Application>())
            _state.update { it.copy(photos = uris, isLoading = false) }
        }
    }

    private fun selectPhoto(uri: Uri) {
        _state.update { it.copy(selectedPhoto = uri) }
    }

    private fun goNext() {
        viewModelScope.launch { _effect.send(PickPhotoEffect.NavigateNext) }
    }

    private fun close() {
        viewModelScope.launch { _effect.send(PickPhotoEffect.CloseScreen) }
    }

    private fun getAllImages(context: Context): List<Uri> {
        val uris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                uris.add(uri)
            }
        }
        return uris
    }
}