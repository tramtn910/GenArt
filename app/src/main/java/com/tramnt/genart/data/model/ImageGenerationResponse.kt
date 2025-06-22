package com.tramnt.genart.data.model

import com.google.gson.annotations.SerializedName

data class ImageGenerationResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ImageGenerationData?,
    @SerializedName("timestamp")
    val timestamp: Long
)

data class ImageGenerationData(
    @SerializedName("url")
    val url: String,
    @SerializedName("path")
    val path: String
) 