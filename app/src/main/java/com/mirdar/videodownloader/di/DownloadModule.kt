package com.mirdar.videodownloader.di

import android.content.Context
import androidx.room.Room
import com.mirdar.videodownloader.data.download.DownloadRepositoryImpl
import com.mirdar.videodownloader.data.download.cache.DownloadDao
import com.mirdar.videodownloader.data.download.cache.DownloadDatabase
import com.mirdar.videodownloader.data.download.remote.DownloadApi
import com.mirdar.videodownloader.domain.download.DownloadRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

        @Provides
        @Singleton
        fun provideDownloadDatabase(
            @ApplicationContext context: Context
        ): DownloadDatabase {
            val builder = Room.databaseBuilder(
                context = context,
                klass = DownloadDatabase::class.java,
                name = DownloadDatabase.DATABASE_NAME
            )

            return builder.build()
        }

        @Provides
        @Singleton
        fun provideDownloadDao(database: DownloadDatabase): DownloadDao {
            return database.downloadDao()
        }
    }
}