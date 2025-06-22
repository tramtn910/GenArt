package com.tramnt.genart.ui.pickphoto

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.tramnt.genart.base.mvi.MviViewModel
import com.tramnt.genart.util.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tramnt.genart.util.MediaStoreImagePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class PickPhotoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : MviViewModel<PickPhotoIntent, PickPhotoViewState, PickPhotoEffect>() {

    override val initialState: PickPhotoViewState = PickPhotoViewState()

    private fun createPhotoPagingFlow(): Flow<PagingData<Uri>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { MediaStoreImagePagingSource(context) }
        ).flow.cachedIn(viewModelScope)
    }

    private val _photoPagingFlow = MutableStateFlow(createPhotoPagingFlow())
    val photoPagingFlow: StateFlow<Flow<PagingData<Uri>>> get() = _photoPagingFlow

    override fun processIntent(intent: PickPhotoIntent) {
        when (intent) {
            is PickPhotoIntent.LoadPhotos -> loadPhotos()
            is PickPhotoIntent.SelectPhoto -> selectPhoto(intent.uri)
            is PickPhotoIntent.PermissionResult -> handlePermissionResult(intent.granted)
        }
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val hasPermission = ImageUtils.checkImagePermission(context)
            if (!hasPermission) {
                setState { 
                    copy(
                        hasPermission = false,
                        isLoading = false
                    ) 
                }
                return@launch
            }

            val uris = ImageUtils.getAllImages(context)
            setState { 
                copy(
                    photos = uris, 
                    isLoading = false,
                    hasPermission = true
                ) 
            }
        }
    }

    private fun selectPhoto(uri: Uri) {
        setState { copy(selectedPhoto = uri) }
    }

    private fun handlePermissionResult(granted: Boolean) {
        if (granted) {
            setState { copy(hasPermission = true) }
            _photoPagingFlow.value = createPhotoPagingFlow()
        } else {
            setState { copy(hasPermission = false) }
        }
    }
}