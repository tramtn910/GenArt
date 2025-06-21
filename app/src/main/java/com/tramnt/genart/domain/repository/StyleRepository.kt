package com.tramnt.genart.domain.repository

import com.tramnt.genart.data.model.StyleCategory

interface StyleRepository {
    suspend fun getStyles(): Result<List<StyleCategory>>
} 