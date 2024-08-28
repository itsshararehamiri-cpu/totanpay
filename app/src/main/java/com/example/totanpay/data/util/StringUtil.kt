package com.example.totanpay.data.util

fun String.formatAmount(): String {
    val t = this.replace(",", "")
    return String.format("%,d", t.toLong())
}

fun String?.mask(): String {
    return if (this != null)
        "${take(6)}****${takeLast(4)}" else ""
}

fun getSwitchMessage(code: Int): String {
    return when (code) {
        0 -> "تراکنش موفق"

        else -> "خطای نامشخص"
    }
}

fun String.toTimeFormat():String {
    val temp = StringBuilder()
    temp.append(this.substring(0, 2))
    temp.append(":")
    temp.append(this.substring(2, 4))
    return temp.toString()
}