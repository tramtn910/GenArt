package com.tramnt.genart.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.tramnt.genart.base.mvi.BaseMviActivity
import com.tramnt.genart.util.SystemBarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseMviActivity<HistoryIntent, HistoryViewState, HistoryEffect, HistoryViewModel>() {
    override val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtils.hideSystemBars(this)
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