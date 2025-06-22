package com.tramnt.genart.ui.genart

import androidx.lifecycle.viewModelScope
import com.tramnt.genart.base.mvi.MviViewModel
import com.tramnt.genart.domain.usecase.GenerateImageUseCase
import com.tramnt.genart.domain.usecase.GetStylesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenArtViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase,
    private val generateImageUseCase: GenerateImageUseCase
) : MviViewModel<GenArtIntent, GenArtViewState, GenArtEffect>() {

    override val initialState: GenArtViewState
        get() = GenArtViewState()

    override fun processIntent(intent: GenArtIntent) {
        when (intent) {
            is GenArtIntent.LoadStyles -> {
                viewModelScope.launch { loadStyles() }
            }
            is GenArtIntent.SelectStyleCategory -> selectStyleCategory(intent.category)
            is GenArtIntent.SelectStyleItem -> selectStyleItem(intent.style)
            is GenArtIntent.UpdatePrompt -> {
                setState { copy(prompt = intent.prompt) }
            }
            is GenArtIntent.AddPhoto -> {
                viewModelScope.launch { sendEffect { GenArtEffect.ShowPhotoPicker } }
            }
            is GenArtIntent.PhotoSelected -> {
                setState { copy(photoUri = intent.photoUri) }
            }
            is GenArtIntent.GenerateAI -> {
                viewModelScope.launch { generateAI() }
            }
        }
    }

    private suspend fun generateAI() {
        val currentState = uiState.value
        val photoUri = currentState.photoUri
        val prompt = currentState.prompt
        val selectedStyle = currentState.selectedStyleItem

        if (photoUri == null) {
            sendEffect { GenArtEffect.ShowError("Please select a photo first") }
            return
        }

        setState { copy(isGenerating = true, error = null) }

        generateImageUseCase(
            imageUri = photoUri,
            styleId = selectedStyle?.id,
            prompt = prompt.takeIf { it.isNotBlank() },
            positivePrompt = prompt.takeIf { it.isNotBlank() }
        ).fold(
            onSuccess = { imageUrl ->
                setState { copy(isGenerating = false) }
                sendEffect { GenArtEffect.NavigateToResult(imageUrl) }
            },
            onFailure = { exception ->
                val errorMessage = exception.message ?: "Failed to generate image"
                setState { copy(isGenerating = false, error = errorMessage) }
                sendEffect { GenArtEffect.ShowError(errorMessage) }
            }
        )
    }

    private suspend fun loadStyles() {
        setState { copy(isLoading = true, error = null) }
        
        getStylesUseCase().fold(
            onSuccess = { categories ->
                setState {
                    copy(
                        isLoading = false,
                        styleCategories = categories,
                        selectedCategory = categories.firstOrNull(),
                        selectedStyleItem = categories.firstOrNull()?.styles?.firstOrNull()
                    )
                }
            },
            onFailure = { exception ->
                val errorMessage = exception.message ?: "Unknown error occurred"
                setState { copy(isLoading = false, error = errorMessage) }
                sendEffect { GenArtEffect.ShowError(errorMessage) }
            }
        )
    }

    private fun selectStyleCategory(category: com.tramnt.genart.data.model.StyleCategory) {
        setState {
            copy(
                selectedCategory = category,
                selectedStyleItem = category.styles.firstOrNull()
            )
        }
    }

    private fun selectStyleItem(style: com.tramnt.genart.data.model.Style) {
        setState { copy(selectedStyleItem = style) }
    }
} 