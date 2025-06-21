package com.tramnt.genart.data.model

import com.google.gson.annotations.SerializedName

data class StyleResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: StyleData
)

data class StyleData(
    @SerializedName("items")
    val items: List<StyleCategory>
)

data class StyleCategory(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("styles")
    val styles: List<Style>
)

data class Style(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("key")
    val key: String
) 