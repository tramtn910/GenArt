package com.tramnt.genart.ui.result

import com.tramnt.genart.base.mvi.MviIntent
import com.tramnt.genart.base.mvi.MviViewState
import com.tramnt.genart.base.mvi.MviEffect
import android.content.Context

sealed class ResultIntent : MviIntent {
    data class DownloadImage(val context: Context, val url: String) : ResultIntent()
    object Back : ResultIntent()
}

data class ResultViewState(
    val imageUrl: String = "",
    val isDownloading: Boolean = false,
    val downloadSuccess: Boolean? = null,
    val errorMessage: String? = null
) : MviViewState

sealed class ResultEffect : MviEffect {
    object DownloadSuccess : ResultEffect()
    data class ShowError(val message: String) : ResultEffect()
    object Back : ResultEffect()
} 