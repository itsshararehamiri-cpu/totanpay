package com.example.totanpay.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun convertToTimestamp(dateStr: String): Long {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("Asia/Tehran")
    val date = sdf.parse(dateStr)
    val timestamp = date.time
    return timestamp
}