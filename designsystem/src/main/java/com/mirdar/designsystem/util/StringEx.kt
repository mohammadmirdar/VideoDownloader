package com.mirdar.designsystem.util

import kotlin.math.pow

fun CharSequence.farsilize(): String {
    return StringBuilder(length).apply {
        this@farsilize.forEach {
            when (it) {
                '0' -> append('۰')
                '1' -> append('۱')
                '2' -> append('۲')
                '3' -> append('۳')
                '4' -> append('۴')
                '5' -> append('۵')
                '6' -> append('۶')
                '7' -> append('۷')
                '8' -> append('۸')
                '9' -> append('۹')
                else -> append(it)
            }
        }
    }.toString()
}

fun String.pad(count: Int, padChar: Char = ' '): String {
    return this.padStart(length = this.length + count, padChar = padChar)
        .padEnd(length = this.length + (count * 2), padChar = padChar)
}

private fun Long.padStartWith0() = if (this < MAX_PAD_DIGIT) {
    this.toString().padStart(2, '0')
} else {
    this.toString()
}

fun Char.isContainCombinedLetters(): Boolean {
    val regexPattern = "[0-9A-Za-z\\u0600-\\u06FF\\u0750-\\u077F\\p{S}\\p{P} ]".toRegex()
    return regexPattern.matches(toString())
}

fun String.isUid(): Boolean = this.length <= MAX_LENGTH_UID
private const val MAX_LENGTH_UID = 8

fun Long.calculateTimes(): String {
    val seconds = this / SECONDS_IN_MILLIS
    val contentProgressHours = seconds / (MINUTE_IN_SECONDS.toDouble().pow(2.0)).toLong()
    val contentProgressMinutes = (seconds / MINUTE_IN_SECONDS) % MINUTE_IN_SECONDS
    val contentProgressSeconds = seconds % MINUTE_IN_SECONDS

    val contentProgressHoursStr = contentProgressHours.padStartWith0()
    val contentProgressMinutesStr = contentProgressMinutes.padStartWith0()
    val contentProgressSecondsStr = contentProgressSeconds.padStartWith0()

    return if (contentProgressHours > 0) {
        "$contentProgressHoursStr:$contentProgressMinutesStr:$contentProgressSecondsStr"
    } else {
        "$contentProgressMinutesStr:$contentProgressSecondsStr"
    }
}

private const val MAX_PAD_DIGIT = 10
private const val MINUTE_IN_SECONDS = 60L
private const val SECONDS_IN_MILLIS = 1000L
