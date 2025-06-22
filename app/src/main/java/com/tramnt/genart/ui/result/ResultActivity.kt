package com.tramnt.genart.ui.result

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.tramnt.genart.base.mvi.BaseMviActivity
import dagger.hilt.android.AndroidEntryPoint
import com.tramnt.genart.ui.pickphoto.PickPhotoActivity

@AndroidEntryPoint
class ResultActivity : BaseMviActivity<ResultIntent, ResultViewState, ResultEffect, ResultViewModel>() {
    override val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val prompt = intent.getStringExtra("prompt") ?: ""
        val style = intent.getStringExtra("style")
        viewModel.setImageUrl(imageUrl)
        if (imageUrl.isNotEmpty()) {
            viewModel.addToHistory(imageUrl, prompt, style)
        }
    }

    @Composable
    override fun RenderView(state: ResultViewState?, sendIntent: (ResultIntent) -> Unit) {
        if (state == null) return
        val context = LocalContext.current
        ResultScreen(
            imageUrl = state.imageUrl,
            isDownloading = state.isDownloading,
            onDownload = { sendIntent(ResultIntent.DownloadImage(context, state.imageUrl)) },
            onBack = { sendIntent(ResultIntent.Back) },
            onHistory = {
                val intent = Intent(this, com.tramnt.genart.ui.history.HistoryActivity::class.java)
                startActivity(intent)
            }
        )
    }

    override fun handleEffect(effect: ResultEffect) {
        when (effect) {
            is ResultEffect.DownloadSuccess -> {
                Toast.makeText(this, "Image saved to Gallery", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PickPhotoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            is ResultEffect.ShowError -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
            is ResultEffect.Back -> finish()
        }
    }
}