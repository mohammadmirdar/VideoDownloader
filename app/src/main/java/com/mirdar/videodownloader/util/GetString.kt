package com.mirdar.videodownloader.util

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetString @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    operator fun invoke(@StringRes id: Int): String = context.getString(id)
}