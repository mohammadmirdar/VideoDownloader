package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.di

import android.content.Context
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.adapter.DownloadStatusJsonAdapter
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.notification.DownloadManagerNotification
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepositoryImpl
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.source.DownloadLocalDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

const val DOWNLOAD_MOSHI_ADAPTER = "download_moshi_adapter"
const val DOWNLOAD_NAME_SPACE = "AparatDownloadNameSpace"

@Module
@InstallIn(SingletonComponent::class)
object DownloadManagerModule {

    @Provides
    @Singleton
    fun provideDownloadManagerConfiguration(
        @ApplicationContext context: Context,
    ): FetchConfiguration {
        return FetchConfiguration.Builder(context)
            .setNamespace(DOWNLOAD_NAME_SPACE)
            .setDownloadConcurrentLimit(1)
            .setAutoRetryMaxAttempts(3)
            .setNotificationManager(DownloadManagerNotification(context = context))
            .build()
    }

    @Provides
    @Singleton
    fun provideDownloadManager(
        configuration: FetchConfiguration,
    ): Fetch {
        return Fetch.getInstance(configuration)
    }

    @Provides
    @Singleton
    fun provideDownloadRepository(
        local: DownloadLocalDataSource,
    ): DownloadRepository {
        return DownloadRepositoryImpl(local = local)
    }

    @Provides
    @Singleton
    @Named(DOWNLOAD_MOSHI_ADAPTER)
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(DownloadStatusJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
