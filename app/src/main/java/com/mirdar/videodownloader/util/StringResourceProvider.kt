package com.mirdar.videodownloader.util

import android.content.res.Resources
import androidx.annotation.StringRes

interface StringResourceProvider {
    fun getString(@StringRes resourceId: Int, parameter: String): String
    fun getString(@StringRes resourceId: Int, vararg parameter: Any): String
}

class StringResourceProviderImpl(
    private val resources: Resources
) : StringResourceProvider {

    override fun getString(
        resourceId: Int,
        parameter: String
    ): String = resources.getString(resourceId, parameter)

    override fun getString(
        resourceId: Int,
        vararg parameter: Any
    ): String = resources.getString(resourceId, *parameter)
}
