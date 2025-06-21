package com.tramnt.genart.ui.pickphoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.tramnt.genart.base.mvi.BaseMviActivity
import com.tramnt.genart.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickPhotoActivity :
    BaseMviActivity<PickPhotoIntent, PickPhotoViewState, PickPhotoEffect, PickPhotoViewModel>() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.processIntent(PickPhotoIntent.PermissionResult(true))
        } else {
            viewModel.processIntent(PickPhotoIntent.PermissionResult(false))
            openAppSettings()
        }
    }

    override val viewModel: PickPhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.processIntent(PickPhotoIntent.LoadPhotos)
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.uiState.value.hasPermission) {
            viewModel.processIntent(PickPhotoIntent.LoadPhotos)
        }
    }

    private fun requestAppropriatePermission() {
        val permission = ImageUtils.getRequiredPermission()
        permissionLauncher.launch(permission)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    @Composable
    override fun RenderView(state: PickPhotoViewState, sendIntent: (PickPhotoIntent) -> Unit) {
        PickPhotoScreen(
            state = state,
            onBackClick = {
                finish()
            },
            onNextClick = {
                navigateNext()
            },
            onRequestPermission = {
                requestAppropriatePermission()
            },
            onIntent = sendIntent
        )
    }
    
    private fun navigateNext() {
        val selectedPhoto = viewModel.uiState.value.selectedPhoto
        if (selectedPhoto != null) {
            val resultIntent = Intent().apply {
                putExtra("selected_photo_uri", selectedPhoto.toString())
            }
            setResult(RESULT_OK, resultIntent)
        }
        finish()
    }
}