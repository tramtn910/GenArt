package com.tramnt.genart.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.tramnt.genart.base.mvi.BaseMviActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.Composable

@AndroidEntryPoint
class HistoryActivity : BaseMviActivity<HistoryIntent, HistoryViewState, HistoryEffect, HistoryViewModel>() {
    override val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.processIntent(HistoryIntent.LoadHistory)
    }
    @Composable
    override fun RenderView(state: HistoryViewState?, sendIntent: (HistoryIntent) -> Unit) {
        if (state != null) {
            HistoryScreen(
                historyImages = state.historyImages,
                onDelete = { sendIntent(HistoryIntent.DeleteHistory(it)) },
                onClearAll = { sendIntent(HistoryIntent.ClearAll) }
            )
        }
    }

    override fun handleEffect(effect: HistoryEffect) {
        when (effect) {
            is HistoryEffect.ShowMessage -> Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
        }
    }
} 