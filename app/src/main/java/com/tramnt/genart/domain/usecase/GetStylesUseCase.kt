package com.tramnt.genart.domain.usecase

import com.tramnt.genart.data.model.StyleCategory
import com.tramnt.genart.domain.repository.StyleRepository
import javax.inject.Inject

class GetStylesUseCase @Inject constructor(
    private val repository: StyleRepository
) {
    suspend operator fun invoke(): Result<List<StyleCategory>> {
        return repository.getStyles()
    }
} 