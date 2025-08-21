package com.mirdar.designsystem.util

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun Date.remainTime(): Long = time.remainTime()

fun Long.remainTime(): Long {
    val tehranTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Tehran")
    val tehranTime = Calendar.getInstance(tehranTimeZone).timeInMillis
    return this - tehranTime
}
