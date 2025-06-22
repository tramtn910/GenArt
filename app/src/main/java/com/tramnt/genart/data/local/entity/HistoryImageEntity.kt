package com.tramnt.genart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_images")
data class HistoryImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val prompt: String,
    val style: String?,
    val createdAt: Long = System.currentTimeMillis()
) 