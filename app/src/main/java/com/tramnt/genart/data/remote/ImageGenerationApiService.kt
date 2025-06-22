package com.tramnt.genart.data.remote

import com.tramnt.genart.data.model.ImageGenerationRequest
import com.tramnt.genart.data.model.ImageGenerationResponse
import com.tramnt.genart.data.model.PresignedLinkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ImageGenerationApiService {
    
    @GET("api/v5/image-ai/presigned-link")
    suspend fun getPresignedLink(): Response<PresignedLinkResponse>
    
    @POST("api/v5/image-ai")
    suspend fun generateImage(@Body request: ImageGenerationRequest): Response<ImageGenerationResponse>
} 