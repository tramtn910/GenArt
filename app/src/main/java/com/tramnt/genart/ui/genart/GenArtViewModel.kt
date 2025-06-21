package com.tramnt.genart.ui.genart

import androidx.lifecycle.viewModelScope
import com.tramnt.genart.base.mvi.MviViewModel
import com.tramnt.genart.domain.usecase.GetStylesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenArtViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase
) : MviViewModel<GenArtIntent, GenArtViewState, GenArtEffect>() {

    override val initialState: GenArtViewState
        get() = GenArtViewState()

    override fun processIntent(intent: GenArtIntent) {
        when (intent) {
            is GenArtIntent.LoadStyles -> loadStyles()
            is GenArtIntent.SelectStyleCategory -> selectStyleCategory(intent.category)
            is GenArtIntent.SelectStyleItem -> selectStyleItem(intent.style)
            is GenArtIntent.EnterPrompt -> {
                // Handle prompt input
            }
            is GenArtIntent.AddPhoto -> {
                viewModelScope.launch {
                    sendEffect { GenArtEffect.ShowPhotoPicker }
                }
            }
            is GenArtIntent.PhotoSelected -> {
                setState { copy(photoUri = intent.photoUri) }
            }
            is GenArtIntent.SelectStyle -> {
                // Handle style selection
            }
            is GenArtIntent.GenerateAI -> {
                // Handle AI generation
            }
            else -> {}
        }
    }

    private fun loadStyles() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            
            getStylesUseCase().fold(
                onSuccess = { categories ->
                    setState {
                        copy(
                            isLoading = false,
                            styleCategories = categories,
                            selectedCategory = categories.firstOrNull()
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
    }

    private fun selectStyleCategory(category: com.tramnt.genart.data.model.StyleCategory) {
        setState { copy(selectedCategory = category, selectedStyleItem = category.styles.firstOrNull()) }
    }

    private fun selectStyleItem(style: com.tramnt.genart.data.model.Style) {
        setState { copy(selectedStyleItem = style) }
    }
} 