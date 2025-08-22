package com.mirdar.videodownloader.di

import com.mirdar.videodownloader.data.home.HomeRepositoryImpl
import com.mirdar.videodownloader.data.home.HomeService
import com.mirdar.videodownloader.domain.home.HomeRepository
import com.mirdar.videodownloader.domain.home.usecase.GetVideoInfoUseCase
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
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun provideHomeService(retrofit: Retrofit): HomeService = retrofit.create()

        @Provides
        @Singleton
        fun provideGetVideoInfoUseCase(homeRepository: HomeRepository) =
            GetVideoInfoUseCase(homeRepository = homeRepository)
    }
}