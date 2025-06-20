package com.tramnt.genart.ui.genart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GenArtViewModel : ViewModel() {
    private val _state = MutableStateFlow(GenArtViewState())
    val state: StateFlow<GenArtViewState> = _state

    fun onIntent(intent: GenArtIntent) {
        // Chưa xử lý logic
    }
} 