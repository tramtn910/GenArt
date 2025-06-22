package com.tramnt.genart.ui.history

import com.tramnt.genart.base.mvi.MviIntent
import com.tramnt.genart.base.mvi.MviViewState
import com.tramnt.genart.base.mvi.MviEffect
import com.tramnt.genart.data.local.entity.HistoryImageEntity

sealed class HistoryIntent : MviIntent {
    object LoadHistory : HistoryIntent()
    data class AddHistory(val image: HistoryImageEntity) : HistoryIntent()
    data class DeleteHistory(val image: HistoryImageEntity) : HistoryIntent()
    object ClearAll : HistoryIntent()
}

data class HistoryViewState(
    val historyImages: List<HistoryImageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : MviViewState

sealed class HistoryEffect : MviEffect {
    data class ShowMessage(val message: String) : HistoryEffect()
} 