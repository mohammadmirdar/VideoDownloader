package com.mirdar.videodownloader.com.mirdar.videodownloader.di

import com.mirdar.videodownloader.data.download.DownloadRepositoryImpl
import com.mirdar.videodownloader.com.mirdar.videodownloader.data.download.DownloadApi
import com.mirdar.videodownloader.domain.download.DownloadRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadModule {

    @Binds
    @Singleton
    abstract fun bindDownloadRepository(downloadRepositoryImpl: DownloadRepositoryImpl): DownloadRepository

    companion object {
        @Provides
        @Singleton
        fun provideDownloadApi(retrofit: Retrofit): DownloadApi = retrofit.create()
    }
}