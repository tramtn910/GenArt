package com.tramnt.genart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tramnt.genart.data.local.dao.HistoryImageDao
import com.tramnt.genart.data.local.entity.HistoryImageEntity

@Database(entities = [HistoryImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyImageDao(): HistoryImageDao
} 