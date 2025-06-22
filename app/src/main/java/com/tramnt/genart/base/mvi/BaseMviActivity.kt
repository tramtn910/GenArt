package com.tramnt.genart.base.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner

abstract class BaseMviActivity<I : MviIntent, S : MviViewState, E : MviEffect, VM : MviViewModel<I, S, E>>: ComponentActivity() {

    protected abstract val viewModel: VM

    @Composable
    abstract fun RenderView(state: S?, sendIntent: (I) -> Unit)

    open fun handleEffect(effect: E) {}
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.uiState.collectAsState()
            val lifecycle = LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(lifecycle.currentState) {
                viewModel.uiEffect.collectLatest { effect ->
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        handleEffect(effect)
                    }
                }
            }
            RenderView(state = state, sendIntent = viewModel::processIntent)
        }
    }
}