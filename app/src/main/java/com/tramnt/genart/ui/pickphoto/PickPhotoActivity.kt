package com.tramnt.genart.ui.pickphoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class PickPhotoActivity : ComponentActivity() {
    private val viewModel: PickPhotoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PickPhotoViewModel(application) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is PickPhotoEffect.NavigateNext -> {
                            // TODO: Trả về ảnh đã chọn qua intent hoặc chuyển màn hình
                        }
                        is PickPhotoEffect.CloseScreen -> finish()
                    }
                }
            }

            PickPhotoScreen(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}