package com.tramnt.genart.ui.genart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tramnt.genart.base.mvi.BaseMviActivity
import com.tramnt.genart.ui.theme.GenArtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenArtActivity : BaseMviActivity<GenArtIntent, GenArtViewState, GenArtEffect, GenArtViewModel>() {

    override val viewModel: GenArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @Composable
    override fun RenderView(state: GenArtViewState, sendIntent: (GenArtIntent) -> Unit) {
        GenArtTheme {
            GenArtScreen(
                state = state,
                onIntent = sendIntent
            )
        }
    }

    override fun handleEffect(effect: GenArtEffect) {
        when (effect) {
            is GenArtEffect.ShowError -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}

