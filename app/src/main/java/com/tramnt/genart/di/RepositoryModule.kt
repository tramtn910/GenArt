package com.tramnt.genart.di

import com.tramnt.genart.data.repository.ImageGenerationRepositoryImpl
import com.tramnt.genart.data.repository.StyleRepositoryImpl
import com.tramnt.genart.domain.repository.ImageGenerationRepository
import com.tramnt.genart.domain.repository.StyleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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