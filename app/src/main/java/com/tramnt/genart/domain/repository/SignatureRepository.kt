package com.tramnt.genart.domain.repository

import android.util.Log
import com.apero.aigenerate.AiServiceConfig
import com.tramnt.genart.data.remote.SignatureApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignatureRepository @Inject constructor(
    private val signatureApiService: SignatureApiService
) {

    suspend fun testAuthentication(): Result<String> {
        return try {
            // Log current timestamp before making request
            Log.d("SignatureRepository", "Current timestamp before request: ${AiServiceConfig.timeStamp}")
            Log.d("SignatureRepository", "API Key: ${AiServiceConfig.apiKey}")
            
            val response = signatureApiService.testAuthentication()

            if (response.isSuccessful) {
                val body = response.body()
                // Add debug logging
                Log.d("SignatureRepository", "Response body: $body")
                Log.d("SignatureRepository", "StatusCode: ${body?.statusCode}, Message: ${body?.message}")
                Log.d("SignatureRepository", "Server timestamp: ${body?.timestamp}")
                
                // Always update timestamp from server response to sync time
                body?.timestamp?.let { timestamp ->
                    AiServiceConfig.setTimeStamp(timestamp)
                    Log.d("SignatureRepository", "Updated timestamp to: $timestamp")
                }
                
                if (body?.statusCode == 200 && body.message == "success") {
                    val presignedUrl = body.data?.url
                    if (presignedUrl != null) {
                        Result.success("Authentication successful: ${body.message}. Presigned URL: $presignedUrl")
                    } else {
                        Result.failure(Exception("Authentication successful but no presigned URL received"))
                    }
                } else {
                    // Log more details about the failure
                    Log.e("SignatureRepository", "Authentication failed. StatusCode: ${body?.statusCode}, Message: ${body?.message}")
                    Result.failure(Exception("Authentication failed: ${body?.message ?: "Unknown error"}"))
                }
            } else {
                Log.e("SignatureRepository", "Network error: ${response.code()} ${response.message()}")
                Result.failure(Exception("Network error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("SignatureRepository", "Connection error: ${e.message}", e)
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }
}