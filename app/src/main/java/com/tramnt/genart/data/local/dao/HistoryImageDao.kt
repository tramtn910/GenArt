package com.tramnt.genart.data.local.dao

import androidx.room.*
import com.tramnt.genart.data.local.entity.HistoryImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: HistoryImageEntity)

    @Query("SELECT * FROM history_images ORDER BY createdAt DESC")
    fun getAll(): Flow<List<HistoryImageEntity>>

    @Delete
    suspend fun delete(image: HistoryImageEntity)

    @Query("DELETE FROM history_images")
    suspend fun clearAll()
} 