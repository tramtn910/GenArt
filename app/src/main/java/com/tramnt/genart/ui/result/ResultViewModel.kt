package com.tramnt.genart.ui.result

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.tramnt.genart.base.mvi.MviViewModel
import com.tramnt.genart.data.local.entity.HistoryImageEntity
import com.tramnt.genart.util.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val historyImageDao: com.tramnt.genart.data.local.dao.HistoryImageDao
) : MviViewModel<ResultIntent, ResultViewState, ResultEffect>() {
    override val initialState = ResultViewState()

    fun setImageUrl(url: String) {
        setState { copy(imageUrl = url) }
    }

    fun addToHistory(url: String, prompt: String, style: String?) {
        viewModelScope.launch {
            val entity = HistoryImageEntity(
                url = url,
                prompt = prompt,
                style = style
            )
            historyImageDao.insert(entity)
        }
    }

    override fun processIntent(intent: ResultIntent) {
        when (intent) {
            is ResultIntent.DownloadImage -> {
                setState { copy(isDownloading = true, downloadSuccess = null, errorMessage = null) }
                viewModelScope.launch {
                    val result = downloadImageInternal(intent.context, intent.url)
                    if (result.isSuccess) {
                        setState { copy(isDownloading = false, downloadSuccess = true) }
                        sendEffect { ResultEffect.DownloadSuccess }
                    } else {
                        setState {
                            copy(
                                isDownloading = false,
                                downloadSuccess = false,
                                errorMessage = result.exceptionOrNull()?.message
                            )
                        }
                        sendEffect {
                            ResultEffect.ShowError(
                                result.exceptionOrNull()?.message ?: "Unknown error"
                            )
                        }
                    }
                }
            }

            is ResultIntent.Back -> {
                viewModelScope.launch { sendEffect { ResultEffect.Back } }
            }
        }
    }

    private suspend fun downloadImageInternal(context: Context, imageUrl: String): Result<android.net.Uri> {
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = (loader.execute(request) as? SuccessResult)?.drawable
            val bitmap = (result as? BitmapDrawable)?.bitmap
            if (bitmap != null) {
                val displayName = "GenArt_${System.currentTimeMillis()}"
                return ImageUtils.saveBitmap(context, bitmap, displayName)
            } else {
                return Result.failure(Exception("Failed to load image"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
} 