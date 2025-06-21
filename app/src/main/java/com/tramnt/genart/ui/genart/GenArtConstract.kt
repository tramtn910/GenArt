package com.tramnt.genart.ui.genart

import com.tramnt.genart.base.mvi.MviEffect
import com.tramnt.genart.base.mvi.MviIntent
import com.tramnt.genart.base.mvi.MviViewState
import com.tramnt.genart.data.model.Style
import com.tramnt.genart.data.model.StyleCategory

sealed class GenArtIntent : MviIntent {
    data object EnterPrompt : GenArtIntent()
    data object AddPhoto : GenArtIntent()
    data object SelectStyle : GenArtIntent()
    data object GenerateAI : GenArtIntent()
    data object LoadStyles : GenArtIntent()
    data class SelectStyleCategory(val category: StyleCategory) : GenArtIntent()
    data class SelectStyleItem(val style: Style) : GenArtIntent()
    data class PhotoSelected(val photoUri: String) : GenArtIntent()
}

data class GenArtViewState(
    val prompt: String = "",
    val photoUri: String? = null,
    val selectedStyle: String? = null,
    val styleCategories: List<StyleCategory> = emptyList(),
    val selectedCategory: StyleCategory? = null,
    val selectedStyleItem: Style? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : MviViewState

sealed class GenArtEffect : MviEffect {
    data object ShowPhotoPicker : GenArtEffect()
    data class ShowError(val message: String) : GenArtEffect()
} 