package com.tramnt.genart.data.remote

import com.tramnt.genart.data.model.SignatureResponse
import retrofit2.Response
import retrofit2.http.GET

interface SignatureApiService {
    @GET("/api/v5/image-ai/presigned-link")
    suspend fun testAuthentication(): Response<SignatureResponse>
}