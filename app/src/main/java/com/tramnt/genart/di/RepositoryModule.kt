package com.tramnt.genart.di

import android.content.Context
import androidx.room.Room
import com.tramnt.genart.data.local.AppDatabase
import com.tramnt.genart.data.local.dao.HistoryImageDao
import com.tramnt.genart.data.repository.ImageGenerationRepositoryImpl
import com.tramnt.genart.data.repository.StyleRepositoryImpl
import com.tramnt.genart.domain.repository.ImageGenerationRepository
import com.tramnt.genart.domain.repository.StyleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStyleRepository(
        styleRepositoryImpl: StyleRepositoryImpl
    ): StyleRepository

    @Binds
    abstract fun bindImageGenerationRepository(
        imageGenerationRepositoryImpl: ImageGenerationRepositoryImpl
    ): ImageGenerationRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "genart_db").build()

    @Provides
    fun provideHistoryImageDao(db: AppDatabase): HistoryImageDao = db.historyImageDao()
} 