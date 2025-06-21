package com.tramnt.genart.data.repository

import com.tramnt.genart.data.model.StyleCategory
import com.tramnt.genart.data.remote.StyleApiService
import com.tramnt.genart.domain.repository.StyleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StyleRepositoryImpl @Inject constructor(
    private val apiService: StyleApiService
) : StyleRepository {
    
    override suspend fun getStyles(): Result<List<StyleCategory>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getStyles()
            if (response.success) {
                Result.success(response.data.items)
            } else {
                Result.failure(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 