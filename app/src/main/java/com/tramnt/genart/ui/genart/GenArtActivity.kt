package com.tramnt.genart.ui.genart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.tramnt.genart.base.mvi.BaseMviActivity
import com.tramnt.genart.ui.pickphoto.PickPhotoActivity
import com.tramnt.genart.ui.result.ResultActivity
import com.tramnt.genart.ui.theme.GenArtTheme
import dagger.hilt.android.AndroidEntryPoint
import com.tramnt.genart.util.SystemBarUtils

@AndroidEntryPoint
class GenArtActivity : BaseMviActivity<GenArtIntent, GenArtViewState, GenArtEffect, GenArtViewModel>() {

    override val viewModel: GenArtViewModel by viewModels()

    private val photoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedPhotoUri = result.data?.getStringExtra("selected_photo_uri")
            if (selectedPhotoUri != null) {
                viewModel.processIntent(GenArtIntent.PhotoSelected(selectedPhotoUri))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        SystemBarUtils.hideSystemBars(this)
    }

    @Composable
    override fun RenderView(state: GenArtViewState?, sendIntent: (GenArtIntent) -> Unit) {
        GenArtTheme {
            if (state != null) {
                GenArtScreen(
                    state = state,
                    onIntent = sendIntent
                )
            }
        }
    }

    override fun handleEffect(effect: GenArtEffect) {
        when (effect) {
            is GenArtEffect.ShowError -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
            is GenArtEffect.ShowPhotoPicker -> {
                val intent = Intent(this, PickPhotoActivity::class.java)
                photoPickerLauncher.launch(intent)
            }
            is GenArtEffect.NavigateToResult -> {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("image_url", effect.imageUrl)
                    putExtra("prompt", viewModel.uiState.value.prompt)
                    putExtra("style", viewModel.uiState.value.selectedStyleItem?.name)
                }
                startActivity(intent)
            }
        }
    }
}