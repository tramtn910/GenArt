package com.tramnt.genart.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.tramnt.genart.data.model.ImageGenerationRequest
import com.tramnt.genart.data.remote.ImageGenerationApiService
import com.tramnt.genart.domain.repository.ImageGenerationRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class ImageGenerationRepositoryImpl @Inject constructor(
    private val imageGenerationApiService: ImageGenerationApiService,
    private val context: Context
) : ImageGenerationRepository {

    override suspend fun generateImage(
        imageUri: String,
        styleId: String?,
        prompt: String?,
        positivePrompt: String?,
        negativePrompt: String?,
        alpha: Float?,
        strength: Float?,
        mode: Int,
        type: String
    ): Result<String> {
        return try {
            Log.d("ImageGenerationRepository", "=== Starting image generation ===")
            Log.d("ImageGenerationRepository", "Input params: imageUri=$imageUri, styleId=$styleId, prompt=$prompt, mode=$mode, type=$type")
            
            // Step 1: Get presigned link
            Log.d("ImageGenerationRepository", "Step 1: Getting presigned link...")
            val presignedResponse = imageGenerationApiService.getPresignedLink()
            
            Log.d("ImageGenerationRepository", "Presigned response code: ${presignedResponse.code()}")
            Log.d("ImageGenerationRepository", "Presigned response body: ${presignedResponse.body()}")
            
            if (!presignedResponse.isSuccessful) {
                Log.e("ImageGenerationRepository", "Failed to get presigned link: ${presignedResponse.code()}")
                return Result.failure(Exception("Failed to get presigned link: ${presignedResponse.code()}"))
            }
            
            val presignedData = presignedResponse.body()
            if (presignedData?.data?.url == null) {
                Log.e("ImageGenerationRepository", "Presigned URL is null")
                return Result.failure(Exception("Presigned URL is null"))
            }
            
            val presignedUrl = presignedData.data.url
            val filePath = presignedData.data.path
            
            Log.d("ImageGenerationRepository", "Presigned URL: $presignedUrl")
            Log.d("ImageGenerationRepository", "File path: $filePath")
            
            // Step 2: Upload image to presigned URL
            Log.d("ImageGenerationRepository", "Step 2: Uploading image to presigned URL...")
            val uploadResult = uploadImageToPresignedUrl(imageUri, presignedUrl)
            if (uploadResult.isFailure) {
                Log.e("ImageGenerationRepository", "Upload failed: ${uploadResult.exceptionOrNull()?.message}")
                return Result.failure(uploadResult.exceptionOrNull() ?: Exception("Upload failed"))
            }
            
            // Step 3: Generate image
            Log.d("ImageGenerationRepository", "Step 3: Generating image...")
            val request = ImageGenerationRequest(
                file = filePath,
                styleId = styleId,
                positivePrompt = positivePrompt,
                negativePrompt = negativePrompt,
                alpha = alpha,
                strength = strength,
                prompt = prompt,
                mode = mode,
                type = type
            )
            
            Log.d("ImageGenerationRepository", "Generation request: $request")
            val response = imageGenerationApiService.generateImage(request)
            
            Log.d("ImageGenerationRepository", "Generation response code: ${response.code()}")
            Log.d("ImageGenerationRepository", "Generation response body: ${response.body()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("ImageGenerationRepository", "Generation response: $body")
                
                if (body?.statusCode == 200 && body.message == "success") {
                    val imageUrl = body.data?.url
                    if (imageUrl != null) {
                        Log.d("ImageGenerationRepository", "Generated image URL: $imageUrl")
                        Result.success(imageUrl)
                    } else {
                        Log.e("ImageGenerationRepository", "Generated image URL is null")
                        Result.failure(Exception("Generated image URL is null"))
                    }
                } else {
                    Log.e("ImageGenerationRepository", "Generation failed: statusCode=${body?.statusCode}, message=${body?.message}")
                    Result.failure(Exception("Generation failed: ${body?.message}"))
                }
            } else {
                Log.e("ImageGenerationRepository", "Network error: ${response.code()} ${response.message()}")
                Result.failure(Exception("Network error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ImageGenerationRepository", "Error generating image: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    private suspend fun uploadImageToPresignedUrl(imageUri: String, presignedUrl: String): Result<Unit> {
        return try {
            Log.d("ImageGenerationRepository", "Uploading image: $imageUri to: $presignedUrl")
            
            val uri = Uri.parse(imageUri)
            
            // Compress image before upload
            val compressedImageBytes = compressImage(uri)
            Log.d("ImageGenerationRepository", "Compressed image size: ${compressedImageBytes.size()} bytes")
            
            val requestBody = compressedImageBytes.toByteArray().toRequestBody("image/jpeg".toMediaType())
            
            // Create HTTP client with increased timeout for upload
            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
                
            val request = okhttp3.Request.Builder()
                .url(presignedUrl)
                .put(requestBody)
                .build()
            
            Log.d("ImageGenerationRepository", "Executing upload request with increased timeout...")
            
            // Use suspendCancellableCoroutine to avoid NetworkOnMainThreadException
            val response = suspendCancellableCoroutine<okhttp3.Response> { continuation ->
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                        Log.e("ImageGenerationRepository", "Upload request failed", e)
                        continuation.resumeWithException(e)
                    }
                    
                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        Log.d("ImageGenerationRepository", "Upload response code: ${response.code}")
                        continuation.resume(response)
                    }
                })
            }
            
            Log.d("ImageGenerationRepository", "Upload response body: ${response.body?.string()}")
            
            if (response.isSuccessful) {
                Log.d("ImageGenerationRepository", "Image uploaded successfully")
                Result.success(Unit)
            } else {
                Log.e("ImageGenerationRepository", "Upload failed: ${response.code}")
                Result.failure(Exception("Upload failed: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e("ImageGenerationRepository", "Error uploading image: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    private fun compressImage(uri: Uri): ByteArrayOutputStream {
        // First, read the image to get dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        
        // Create a copy of the input stream for the first read
        val inputStreamCopy = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStreamCopy, null, options)
        inputStreamCopy?.close()
        
        // Calculate sample size to reduce memory usage
        val maxSize = 1024 // Max width/height
        val sampleSize = Math.max(1, Math.min(
            options.outWidth / maxSize,
            options.outHeight / maxSize
        ))
        
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        
        // Read the image again with the calculated sample size
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, decodeOptions)
        inputStream?.close()
        
        val outputStream = ByteArrayOutputStream()
        
        // Compress with quality 80%
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        bitmap?.recycle()
        
        return outputStream
    }
} 