package com.tramnt.genart.base.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<I : MviIntent, S : MviViewState, E : MviEffect> : ViewModel() {

    abstract val initialState: S

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _uiEffect = Channel<E>(Channel.BUFFERED)
    val uiEffect: Flow<E> = _uiEffect.receiveAsFlow()

    init {
        // Ensure initial state is set
        if (_uiState.value != initialState) {
            _uiState.value = initialState
        }
    }

    protected fun setState(reducer: S.() -> S) {
        val currentState = uiState.value
        if (currentState != null) {
            _uiState.update { it.reducer() }
        } else {
            // If state is somehow null, use initialState as base
            _uiState.value = initialState.reducer()
        }
    }

    protected suspend fun sendEffect(builder: () -> E) {
        _uiEffect.trySend(builder())
    }

    abstract fun processIntent(intent: I)
}