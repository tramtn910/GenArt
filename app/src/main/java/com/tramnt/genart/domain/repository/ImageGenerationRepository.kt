package com.tramnt.genart.domain.repository

interface ImageGenerationRepository {
    suspend fun generateImage(
        imageUri: String,
        styleId: String? = null,
        prompt: String? = null,
        positivePrompt: String? = null,
        negativePrompt: String? = null,
        alpha: Float? = null,
        strength: Float? = null,
        mode: Int = 0,
        type: String = "image-to-image"
    ): Result<String>
} 