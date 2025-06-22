package com.tramnt.genart.data.model

import com.google.gson.annotations.SerializedName

data class ImageGenerationRequest(
    @SerializedName("file")
    val file: String,
    @SerializedName("styleId")
    val styleId: String? = null,
    @SerializedName("positivePrompt")
    val positivePrompt: String? = null,
    @SerializedName("negativePrompt")
    val negativePrompt: String? = null,
    @SerializedName("alpha")
    val alpha: Float? = null,
    @SerializedName("strength")
    val strength: Float? = null,
    @SerializedName("guidanceScale")
    val guidanceScale: Float? = null,
    @SerializedName("prompt")
    val prompt: String? = null,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("seed")
    val seed: Int? = null,
    @SerializedName("acceptNSFW")
    val acceptNSFW: Boolean? = null,
    @SerializedName("mode")
    val mode: Int? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("imageSize")
    val imageSize: Int? = null,
    @SerializedName("baseModel")
    val baseModel: String? = null
) 