package com.tramnt.genart.data.model

data class SignatureResponse(
    val statusCode: Int,
    val message: String,
    val data: PresignedLinkData?,
    val timestamp: Long?
)

data class PresignedLinkData(
    val url: String,
    val path: String
)

