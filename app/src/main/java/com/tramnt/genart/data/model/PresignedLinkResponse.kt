package com.tramnt.genart.data.model

import com.google.gson.annotations.SerializedName

data class PresignedLinkResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: PresignedLinkData?,
    @SerializedName("timestamp")
    val timestamp: Long
) 