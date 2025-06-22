package com.tramnt.genart.data.remote

import com.google.gson.GsonBuilder
import com.tramnt.genart.domain.repository.SignatureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfig {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-style-manager.apero.vn/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStyleApiService(retrofit: Retrofit): StyleApiService {
        return retrofit.create(StyleApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideSignatureNetworkClient(): SignatureClient =
        SignatureClient()

    @Provides
    @Singleton
    @Named("signature")
    fun provideSignatureRetrofit(signatureNetworkClient: SignatureClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://api-img-gen-wrapper.apero.vn")
            .client(signatureNetworkClient.createHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideSignatureApiService(@Named("signature") retrofit: Retrofit): SignatureApiService =
        retrofit.create(SignatureApiService::class.java)

    @Provides
    @Singleton
    fun provideSignatureRepository(signatureApiService: SignatureApiService): SignatureRepository =
        SignatureRepository(signatureApiService)
} 