package com.tramnt.genart.base.mvi

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner

abstract class BaseMviFragment<I : MviIntent, S : MviViewState, E : MviEffect, VM : MviViewModel<I, S, E>>: Fragment() {

    protected abstract val viewModel: VM

    @Composable
    abstract fun RenderView(state: S?, sendIntent: (I) -> Unit)

    open fun handleEffect(effect: E) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
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
}