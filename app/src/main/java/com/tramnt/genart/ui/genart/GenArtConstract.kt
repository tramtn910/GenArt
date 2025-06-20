package com.tramnt.genart.ui.genart

sealed class GenArtIntent {
    object EnterPrompt : GenArtIntent()
    object AddPhoto : GenArtIntent()
    object SelectStyle : GenArtIntent()
    object GenerateAI : GenArtIntent()
}

data class GenArtViewState(
    val prompt: String = "",
    val photoUri: String? = null,
    val selectedStyle: String? = null
)

sealed class GenArtEffect {
    object ShowPromptInput : GenArtEffect()
    object ShowPhotoPicker : GenArtEffect()
    object ShowStyleSelector : GenArtEffect()
    object ShowGenerateResult : GenArtEffect()
} 