package com.mirdar.videodownloader.di

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.util.DateAdapter
import com.mirdar.videodownloader.util.interceptor.UserAgentInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIME_OUT = 30L

    @Provides
    fun provideLoggingInterceptor(
        config: AppConfig
    ) = HttpLoggingInterceptor().setLevel(
        if (config.isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    )

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(DateAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }


    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    fun provideRetrofit(
        config: AppConfig,
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(config.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(EitherCallAdapterFactory.create())
            .build()
    }
}
