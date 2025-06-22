package com.tramnt.genart.domain.usecase

import com.tramnt.genart.domain.repository.ImageGenerationRepository
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(
    private val imageGenerationRepository: ImageGenerationRepository
) {
    suspend operator fun invoke(
        imageUri: String,
        styleId: String? = null,
        prompt: String? = null,
        positivePrompt: String? = null,
        negativePrompt: String? = null,
        alpha: Float? = null,
        strength: Float? = null,
        mode: Int = 0,
        type: String = "image-to-image"
    ): Result<String> {
        return imageGenerationRepository.generateImage(
            imageUri = imageUri,
            styleId = styleId,
            prompt = prompt,
            positivePrompt = positivePrompt,
            negativePrompt = negativePrompt,
            alpha = alpha,
            strength = strength,
            mode = mode,
            type = type
        )
    }
} 