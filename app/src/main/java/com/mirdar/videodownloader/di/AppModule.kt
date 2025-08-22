package com.mirdar.videodownloader.di

import android.content.Context
import com.mirdar.videodownloader.BuildConfig
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.util.StringResourceProvider
import com.mirdar.videodownloader.util.StringResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideAppConfig() = AppConfig(
        isMobile = false,
        appName = "FilimoSchoolTv",
        store = "",
        appId = BuildConfig.APPLICATION_ID,
        isDebug = BuildConfig.DEBUG,
        baseUrl = BuildConfig.KEY_BASE_URL,
        storeLink = "",
        surturUrl = "",
        deviceType = BuildConfig.KEY_DEVICE_TYPE,
        versionCode = BuildConfig.VERSION_CODE,
        versionName = BuildConfig.VERSION_NAME,
        applicationId = BuildConfig.APPLICATION_ID,
    )

    @Provides
    fun provideStringResourceProvider(@ApplicationContext context: Context): StringResourceProvider {
        return StringResourceProviderImpl(context.resources)
    }
}
