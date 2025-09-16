package com.mirdar.videodownloader.com.mirdar.videodownloader.util

import androidx.compose.runtime.Composable
import com.mirdar.videodownloader.util.State
import kotlin.apply

@Composable
infix fun <L, R, E> State<L, R, E>.onSuccess(
    fn: @Composable (success: R) -> Unit,
): State<L, R, E> = this.apply { if (this is State.Success) fn(this.data!!) }

@Composable
infix fun <L, R, E> State<L, R, E>.onError(
    fn: @Composable (error: E) -> Unit,
): State<L, R, E> = this.apply { if (this is State.Error) fn(this.error!!) }

@Composable
infix fun <L, R, E> State<L, R, E>.onLoading(
    fn: @Composable (loading: L?) -> Unit,
): State<L, R, E> = this.apply { if (this is State.Loading) fn(this.loading) }
