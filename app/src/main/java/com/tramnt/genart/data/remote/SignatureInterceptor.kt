package com.tramnt.genart.data.remote

import android.util.Log
import com.apero.aigenerate.AiServiceConfig
import com.apero.aiservice.BuildConfig
import com.apero.signature.SignatureParser
import okhttp3.Interceptor
import okhttp3.Response

internal class SignatureInterceptor : Interceptor {
    companion object {
        private const val NOT_GET_API_TOKEN = "NOT_GET_API_TOKEN"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("SignatureInterceptor", "Generating signature with:")
        Log.d("SignatureInterceptor", "API Key: ${AiServiceConfig.apiKey}")
        Log.d("SignatureInterceptor", "Public Key: ${BuildConfig.PUBLIC_KEY}")
        Log.d("SignatureInterceptor", "Timestamp: ${AiServiceConfig.timeStamp}")
        
        val signatureData = SignatureParser.parseData(
            AiServiceConfig.apiKey,
            BuildConfig.PUBLIC_KEY,
            AiServiceConfig.timeStamp
        )
        
        Log.d("SignatureInterceptor", "Generated signature data:")
        Log.d("SignatureInterceptor", "Signature: ${signatureData.signature}")
        Log.d("SignatureInterceptor", "KeyId: ${signatureData.keyId}")
        Log.d("SignatureInterceptor", "TimeStamp: ${signatureData.timeStamp}")
        Log.d("SignatureInterceptor", "TokenIntegrity: ${signatureData.tokenIntegrity}")
        
        val tokenIntegrity = signatureData.tokenIntegrity.ifEmpty { NOT_GET_API_TOKEN }

        val headers = hashMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "device" to "android",
            "x-api-signature" to signatureData.signature,
            "x-api-timestamp" to signatureData.timeStamp.toString(),
            "x-api-keyid" to signatureData.keyId,
            "x-api-token" to tokenIntegrity,
            "x-api-bundleId" to AiServiceConfig.applicationId,
            "App-name" to AiServiceConfig.projectName,
        )
        
        Log.d("SignatureInterceptor", "Request headers:")
        headers.forEach { (key, value) ->
            Log.d("SignatureInterceptor", "$key: $value")
        }
        
        val requestBuilder = chain.request().newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}