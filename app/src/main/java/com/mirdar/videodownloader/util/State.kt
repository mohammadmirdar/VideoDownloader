package com.mirdar.videodownloader.util

import arrow.retrofit.adapter.either.networkhandling.CallError

typealias NetworkState<T> = State<Unit, T, CallError>

sealed class State<L, R, E>(val loading: L?, val data: R?, val error: E?) {
    class Loading<L, R, E>(loading: L? = null) : State<L, R, E>(data = null, error = null, loading = loading)

    class Success<L, R, E>(data: R) : State<L, R, E>(data = data, error = null, loading = null)

    class Error<L, R, E>(error: E) : State<L, R, E>(data = null, loading = null, error = error)
}

fun <L, R, E> R.toSuccess() = State.Success<L, R, E>(this)

fun <L, R, E> E.toError() = State.Error<L, R, E>(this)
