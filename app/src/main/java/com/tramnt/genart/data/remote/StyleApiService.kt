package com.tramnt.genart.data.remote

import com.tramnt.genart.data.model.StyleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StyleApiService {
    @GET("category")
    suspend fun getStyles(
        @Query("project") project: String = "techtrek",
        @Query("segmentValue") segmentValue: String = "IN",
        @Query("styleType") styleType: String = "imageToImage",
        @Query("isApp") isApp: Boolean = true
    ): StyleResponse
} 