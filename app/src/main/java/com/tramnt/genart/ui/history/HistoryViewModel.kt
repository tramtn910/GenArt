package com.tramnt.genart.ui.history

import androidx.lifecycle.viewModelScope
import com.tramnt.genart.base.mvi.MviViewModel
import com.tramnt.genart.data.local.dao.HistoryImageDao
import com.tramnt.genart.data.local.entity.HistoryImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyImageDao: HistoryImageDao
) : MviViewModel<HistoryIntent, HistoryViewState, HistoryEffect>() {
    override val initialState: HistoryViewState = HistoryViewState()

    override fun processIntent(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.LoadHistory -> loadHistory()
            is HistoryIntent.AddHistory -> addHistory(intent.image)
            is HistoryIntent.DeleteHistory -> deleteHistory(intent.image)
            is HistoryIntent.ClearAll -> clearAll()
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            historyImageDao.getAll().collectLatest { list ->
                setState { copy(historyImages = list, isLoading = false) }
            }
        }
    }

    private fun addHistory(image: HistoryImageEntity) {
        viewModelScope.launch {
            historyImageDao.insert(image)
            sendEffect { HistoryEffect.ShowMessage("Added to history") }
        }
    }

    private fun deleteHistory(image: HistoryImageEntity) {
        viewModelScope.launch {
            historyImageDao.delete(image)
            sendEffect { HistoryEffect.ShowMessage("Deleted from history") }
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            historyImageDao.clearAll()
            sendEffect { HistoryEffect.ShowMessage("Cleared all history") }
        }
    }
} 