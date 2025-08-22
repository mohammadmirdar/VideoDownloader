package com.mirdar.videodownloader.util.interceptor

import android.content.res.Resources
import android.os.Build
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.model.UserAgent
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder
import java.util.Locale

class UserAgentInterceptor(
    private val moshi: Moshi,
    private val config: AppConfig,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val device = URLEncoder.encode(
            "${Build.MANUFACTURER}*${Build.MODEL}",
            "UTF-8"
        )
        return runBlocking {
            val screenSize = with(Resources.getSystem().displayMetrics) {
                "${widthPixels}x$heightPixels"
            }
            val userAgent = UserAgent(
                store = config.store,
                screenSize = screenSize,
                appName = config.appName,
                sdk = Build.VERSION.SDK_INT,
                versionCode = config.versionCode,
                versionName = config.versionName,
                locale = Locale.getDefault().language,
                device = device,
            ).run {
                moshi.adapter(UserAgent::class.java).toJson(this)
            }
            requestBuilder.header(USER_AGENT, userAgent)
            requestBuilder.header("Accept", "application/json")
            requestBuilder.header("Accept-Encoding", "gzip, deflate, br")
            chain.proceed(requestBuilder.build())
        }
    }

    companion object {
        private const val USER_AGENT = "UserAgent"
    }
}
